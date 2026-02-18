package dev.keven.ecommerce.modules.cart.presentation.dto.response;

public record CheckoutCartResponse(
        Long orderId,
        String status,
        String totalPrice
) {
}
