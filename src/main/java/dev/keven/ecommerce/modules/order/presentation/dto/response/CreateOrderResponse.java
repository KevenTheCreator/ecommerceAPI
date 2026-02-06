package dev.keven.ecommerce.modules.order.presentation.dto.response;

public record CreateOrderResponse(
        Long orderId,
        String status,
        String totalPrice
) {
}
