package dev.keven.ecommerce.modules.product.application.result;

import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ListProductsResult(
        List<ProductItemResult> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static ListProductsResult from(Page<Product> products) {
        return new ListProductsResult(
                products.getContent().stream()
                        .map(ProductItemResult::from)
                        .toList(),
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isFirst(),
                products.isLast()
        );
    }

    public record ProductItemResult(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static ProductItemResult from(Product product) {
            return new ProductItemResult(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getStatus(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            );
        }
    }
}
