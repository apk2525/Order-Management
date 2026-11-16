package com.furniture.orders.entity;

import com.furniture.orders.domain.OrderItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="order_id")
    private Order order;

    private Long productId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemStatus status;
}
