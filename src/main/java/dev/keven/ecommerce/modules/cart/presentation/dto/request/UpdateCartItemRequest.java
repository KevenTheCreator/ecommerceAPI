package dev.keven.ecommerce.modules.cart.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequest(
        @NotNull @Positive int quantity
) {
}
