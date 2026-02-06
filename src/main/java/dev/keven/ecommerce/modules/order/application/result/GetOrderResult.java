package dev.keven.ecommerce.modules.order.application.result;

import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public record GetOrderResult(
        Long orderId,
        Long userId,
        String status,
        BigDecimal totalPrice,
        List<OrderItemResult> items
) {

    public static GetOrderResult from(Order order) {
        return new GetOrderResult(
                order.getId(),
                order.getUserId(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getItems().stream()
                        .map(OrderItemResult::from)
                        .toList()
        );
    }

    public record OrderItemResult(
            Long productId,
            int quantity,
            BigDecimal price,
            BigDecimal subtotal
    ) {
        public static OrderItemResult from(OrderItem item) {
            return new OrderItemResult(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSubtotal()
            );
        }
    }
}
