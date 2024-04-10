package com.spike.spring.order.web;

import com.spike.spring.order.domain.Order;
import com.spike.spring.order.domain.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Mono<Order> submitOrder(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody OrderRequest orderRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("User: {}", jwt.getClaimAsString("preferred_username"));
        return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
    }
}
