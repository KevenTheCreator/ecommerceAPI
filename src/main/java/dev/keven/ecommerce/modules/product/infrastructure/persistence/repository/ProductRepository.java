package dev.keven.ecommerce.modules.product.infrastructure.persistence.repository;

import dev.keven.ecommerce.modules.product.infrastructure.persistence.entity.ProductEntity;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);

    @Query("""
            SELECT p
            FROM ProductEntity p
            WHERE (:status IS NULL OR p.status = :status)
              AND (:query IS NULL OR :query = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    Page<ProductEntity> search(
            @Param("query") String query,
            @Param("status") ProductStatus status,
            Pageable pageable
    );
}
