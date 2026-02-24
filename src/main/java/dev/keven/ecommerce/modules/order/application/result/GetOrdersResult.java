package dev.keven.ecommerce.modules.order.application.result;

import dev.keven.ecommerce.modules.order.domain.Order;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record GetOrdersResult(
        List<OrderSummaryResult> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static GetOrdersResult from(Page<Order> orders) {
        return new GetOrdersResult(
                orders.getContent().stream().map(OrderSummaryResult::from).toList(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                orders.isFirst(),
                orders.isLast()
        );
    }

    public record OrderSummaryResult(
            Long orderId,
            String status,
            BigDecimal totalPrice,
            LocalDateTime createdAt
    ) {
        public static OrderSummaryResult from(Order order) {
            return new OrderSummaryResult(
                    order.getId(),
                    order.getStatus().name(),
                    order.getTotalPrice(),
                    order.getCreatedAt()
            );
        }
    }
}
