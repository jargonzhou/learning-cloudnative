package com.spike.springframework.flux.domain.service;

import com.spike.springframework.flux.domain.model.Order;
import com.spike.springframework.flux.domain.model.OrderStatus;
import com.spike.springframework.flux.infra.repository.OrderRepository;
import com.spike.springframework.flux.infra.rpc.BookClient;
import com.spike.springframework.flux.infra.rpc.BookDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookClient bookClient;

    public OrderService(OrderRepository orderRepository, BookClient bookClient) {
        this.orderRepository = orderRepository;
        this.bookClient = bookClient;
    }

    // Flux
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Mono
    public Mono<Order> submitOrder(String isbn, int quantity) {
//        return Mono.just(buildRejectedOrder(isbn, quantity))
//                .flatMap(orderRepository::save);

        // 先查询book是否存在
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(isbn, quantity)) // book不存在时
                .flatMap(orderRepository::save);
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }

    public static Order buildAcceptedOrder(BookDto book, int quantity) {
        return Order.of(book.isbn(), book.title() + " - " + book.author(),
                book.price(), quantity, OrderStatus.ACCEPTED);
    }
}
