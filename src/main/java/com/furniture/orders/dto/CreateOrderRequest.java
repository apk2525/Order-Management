package com.furniture.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(@NotNull @Positive Long productId, @Min(1) int quantity) {
}
