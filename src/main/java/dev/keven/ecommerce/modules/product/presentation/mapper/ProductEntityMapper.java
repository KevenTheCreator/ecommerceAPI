package dev.keven.ecommerce.modules.product.presentation.mapper;

import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import dev.keven.ecommerce.modules.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductEntityMapper {

    public ProductEntityMapper() {}

    public static ProductEntity toNewEntity(Product product) {
        return ProductEntity.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(ProductStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static void mapToExistingEntity(Product domain, ProductEntity entity) {
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setStock(domain.getStock());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
    }

    public static Product toDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getStock(),
                productEntity.getStatus(),
                productEntity.getCreatedAt(),
                productEntity.getUpdatedAt()
        );
    }
}
