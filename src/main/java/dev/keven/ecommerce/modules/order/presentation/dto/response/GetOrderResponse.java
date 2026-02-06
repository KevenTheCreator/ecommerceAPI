package dev.keven.ecommerce.modules.order.presentation.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record GetOrderResponse(
        Long orderId,
        Long userId,
        String status,
        BigDecimal totalPrice,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(
            Long productId,
            int quantity,
            BigDecimal price,
            BigDecimal subTotal
    ) {}
}
