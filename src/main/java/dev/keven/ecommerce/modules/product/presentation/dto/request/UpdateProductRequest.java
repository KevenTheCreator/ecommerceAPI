package dev.keven.ecommerce.modules.product.presentation.dto.request;

import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
       @Positive BigDecimal price,
       @PositiveOrZero Integer stock,
        ProductStatus status
) {
}
