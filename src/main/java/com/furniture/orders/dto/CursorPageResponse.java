package com.furniture.orders.dto;

import java.util.List;

public record CursorPageResponse(
        List<OrderResponse> content,
        Long nextCursor,
        boolean hasNext) {
}
