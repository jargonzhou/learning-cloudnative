package com.spike.springframework.flux.web;

import com.spike.springframework.flux.domain.model.Order;
import com.spike.springframework.flux.domain.model.OrderStatus;
import com.spike.springframework.flux.domain.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class) // Web Flux测试
public class OrderControllerTest {
    @Autowired
    private WebTestClient webTestClient; // 测试用WebClient

    @MockitoBean
    private OrderService orderService; // mock服务

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());

        // mock服务的预期行为
        BDDMockito
                .given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder));

        webTestClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange() // 发送请求
                // 预期响应状态
                .expectStatus().is2xxSuccessful()
                // 预期响应体
                .expectBody(Order.class).value(actualOrder -> {
                    Assertions.assertThat(actualOrder).isNotNull();
                    Assertions.assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }
}
