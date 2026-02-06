package dev.keven.ecommerce.modules.order.presentation.dto.response;

public record RemoveItemResponse(
        Long orderId,
        int totalItems,
        String totalPrice
) {
}
