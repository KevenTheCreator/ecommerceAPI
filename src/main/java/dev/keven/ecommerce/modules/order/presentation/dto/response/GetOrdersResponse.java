package dev.keven.ecommerce.modules.order.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record GetOrdersResponse(
        List<OrderSummaryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public record OrderSummaryResponse(
            Long orderId,
            String status,
            BigDecimal totalPrice,
            LocalDateTime createdAt
    ) {
    }
}
