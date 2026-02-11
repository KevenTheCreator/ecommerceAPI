package dev.keven.ecommerce.modules.order.application.result;

import java.math.BigDecimal;

public record AddItemResult(
        Long orderId,
        BigDecimal totalPrice,
        int totalQuantity
) {
}
