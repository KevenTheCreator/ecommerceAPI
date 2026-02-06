package dev.keven.ecommerce.modules.order.presentation.dto.response;

public record CancelOrderResponse(
        Long orderId,
        String status
) {
}
