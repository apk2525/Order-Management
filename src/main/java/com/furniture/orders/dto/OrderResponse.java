package com.furniture.orders.dto;

public record OrderResponse(Long id,String productName,int quantity,String status) {
}
