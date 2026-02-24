package dev.keven.ecommerce.modules.product.application.gateway;

import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductGateway {
    Product save(Product product);
    Product update(Product product);
    Optional<Product> findById(Long id);
    Page<Product> search(
            String query,
            ProductStatus status,
            int page,
            int size
    );
    void deleteById(Long id);
    boolean existsByName(String name);
    boolean existsById(Long id);
}
