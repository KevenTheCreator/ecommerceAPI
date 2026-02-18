package dev.keven.ecommerce.modules.cart.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddItemToCartRequest(
        @NotNull Long productId,
        @NotNull @Positive int quantity
) {
}
