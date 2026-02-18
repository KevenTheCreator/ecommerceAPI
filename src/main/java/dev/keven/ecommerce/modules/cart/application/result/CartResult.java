package dev.keven.ecommerce.modules.cart.application.result;

import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record CartResult(
        Long cartId,
        String status,
        BigDecimal totalPrice,
        int totalQuantity,
        List<CartItemResult> items
) {

    public static CartResult from(Order order) {
        return new CartResult(
                order.getId(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getTotalQuantity(),
                order.getItems().stream().map(CartItemResult::from).toList()
        );
    }

    public record CartItemResult(
            Long productId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {
        public static CartItemResult from(OrderItem item) {
            return new CartItemResult(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSubtotal()
            );
        }
    }
}
