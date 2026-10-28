package com.furniture.orders.controller;

import com.furniture.orders.dto.CreateOrderRequest;
import com.furniture.orders.dto.OrderResponse;
import com.furniture.orders.entity.Order;
import com.furniture.orders.service.OrderCreationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderCreationService orderCreationService;

    public OrderController(OrderCreationService orderCreationService) {
        this.orderCreationService = orderCreationService;
    }

    @GetMapping
    public List<OrderResponse> getOrders() {
        return orderCreationService.getOrderList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
       OrderResponse orderResponse = orderCreationService.getOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder( @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderResponse orderResponse = orderCreationService.createOrder(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
