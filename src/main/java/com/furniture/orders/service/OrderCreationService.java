package com.furniture.orders.service;

import com.furniture.orders.dto.CreateOrderRequest;
import com.furniture.orders.dto.OrderResponse;
import com.furniture.orders.dto.UpdateOrderRequest;
import com.furniture.orders.entity.Order;
import com.furniture.orders.exception.OrderNotFoundException;
import com.furniture.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCreationService {

    private final OrderRepository orderRepository;
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order(createOrderRequest.productId()
                ,createOrderRequest.quantity(),"CREATED");
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(savedOrder.getId()
                ,"Product" + savedOrder.getProductId()
                ,savedOrder.getQuantity()
                ,savedOrder.getStatus());
    }

    public List<OrderResponse> getOrderList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> new OrderResponse(order.getId()
                        ,"Product"+ order.getProductId()
                        ,order.getQuantity()
                        ,order.getStatus()))
                .collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> new OrderResponse(value.getId()
                , "Product" + value.getProductId()
                , value.getQuantity()
                , value.getStatus())).orElseThrow(()->new OrderNotFoundException(id));
    }
@Transactional
    public OrderResponse updateOrderDetails(Long id,UpdateOrderRequest updateOrderRequest) {
        Order order = orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException(id));
            order.setQuantity(updateOrderRequest.quantity());
            order.setProductId(updateOrderRequest.productId());
       //order =  orderRepository.save(order);
       return new OrderResponse(order.getId()
               ,"Product"+order.getProductId()
               ,order.getQuantity()
               ,order.getStatus());
    }

    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException(id));
        orderRepository.deleteById(id);
    }
}
