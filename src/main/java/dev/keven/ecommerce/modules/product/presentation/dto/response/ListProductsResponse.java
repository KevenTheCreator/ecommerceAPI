package dev.keven.ecommerce.modules.product.presentation.dto.response;

import dev.keven.ecommerce.modules.product.domain.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ListProductsResponse(
        List<ProductItemResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public record ProductItemResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }
}
