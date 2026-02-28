package dev.keven.ecommerce.modules.cart.presentation.dto.response;

import java.util.List;

public record CartResponse(
        Long cartId,
        String status,
        String totalPrice,
        int totalQuantity,
        List<CartItemResponse> items
) {
    public record CartItemResponse(
            Long productId,
            int quantity,
            String unitPrice,
            String subtotal,
            boolean available,
            String unavailableReason
    ) {
    }
}
