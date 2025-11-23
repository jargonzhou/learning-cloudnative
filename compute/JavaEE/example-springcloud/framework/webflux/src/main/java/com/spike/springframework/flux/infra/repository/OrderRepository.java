package com.spike.springframework.flux.infra.repository;

import com.spike.springframework.flux.domain.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

// reactive仓库
@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
