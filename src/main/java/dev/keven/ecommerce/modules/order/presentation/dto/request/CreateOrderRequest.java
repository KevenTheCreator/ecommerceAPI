package dev.keven.ecommerce.modules.order.presentation.dto.request;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        List<CreateOrderItemRequest> items
) {
}
