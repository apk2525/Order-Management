package com.furniture.orders.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;
    private String status;

    @OneToMany(mappedBy = "order",cascade = CascadeType.PERSIST)
    List<OrderItem> orderItems = new ArrayList<>();

    @Version
    private Long version;

    public Order(Long productId, int quantity, String status) {
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    public void addItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

}
