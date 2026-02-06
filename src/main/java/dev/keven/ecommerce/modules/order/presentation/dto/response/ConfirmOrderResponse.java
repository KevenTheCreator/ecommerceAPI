package dev.keven.ecommerce.modules.order.presentation.dto.response;

public record ConfirmOrderResponse(
        Long orderId,
        String status,
        int totalItems,
        String totalPrice
) {
}
