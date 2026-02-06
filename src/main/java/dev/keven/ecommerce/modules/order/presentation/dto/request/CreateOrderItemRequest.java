package dev.keven.ecommerce.modules.order.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateOrderItemRequest(
        @NotNull Long productId,
        @NotNull @Positive int quantity,
        @NotNull @Positive BigDecimal price
) {
}
