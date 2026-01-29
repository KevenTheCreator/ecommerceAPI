package dev.keven.ecommerce.modules.product.presentation.dto.response;

import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import java.math.BigDecimal;

public record CreateProductResponse(
        String name, String description, BigDecimal price, Integer stock, ProductStatus status
) {
}
