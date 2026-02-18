package dev.keven.ecommerce.modules.cart.application.result;

public record CheckoutCartResult(
        Long orderId,
        String status,
        String totalPrice
) {
}
