package dev.keven.ecommerce.modules.order.presentation.dto.response;

import java.math.BigDecimal;

public record AddItemResponse(
        Long orderId,
        BigDecimal totalPrice,
        int totalItems
) {
}
