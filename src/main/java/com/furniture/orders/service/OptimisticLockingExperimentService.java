package com.furniture.orders.service;

import com.furniture.orders.entity.Order;
import com.furniture.orders.exception.OrderNotFoundException;
import com.furniture.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class OptimisticLockingExperimentService {

    private final OrderRepository orderRepository;
    public OptimisticLockingExperimentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void updateQuantity(
            Long orderId,
            int quantity,
            CountDownLatch bothTransactionsReady) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        bothTransactionsReady.countDown();

        try {
            if (!bothTransactionsReady.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out waiting for both transactions");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting", e);
        }

        order.setQuantity(quantity);
    }
}
