package dev.keven.ecommerce.modules.product.presentation.dto.response;

import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProductResponse(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductStatus status,
        LocalDateTime updated
) {
}
