package dev.keven.ecommerce.modules.cart.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateCartItemRequest(
        @NotNull @PositiveOrZero int quantity
) {
}
