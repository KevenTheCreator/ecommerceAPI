package dev.keven.ecommerce.modules.product.application.command;

import dev.keven.ecommerce.modules.product.domain.ProductStatus;

public record ListProductsCommand(
        String query,
        ProductStatus status,
        Integer page,
        Integer size,
        boolean includeInactive
) {
}
