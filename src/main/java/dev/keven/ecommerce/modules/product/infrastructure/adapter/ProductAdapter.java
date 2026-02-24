package dev.keven.ecommerce.modules.product.infrastructure.adapter;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import dev.keven.ecommerce.modules.product.infrastructure.persistence.entity.ProductEntity;
import dev.keven.ecommerce.modules.product.infrastructure.persistence.repository.ProductRepository;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class ProductAdapter implements ProductGateway {

    private final ProductRepository repository;

    private final ProductEntityMapper entityMapper;

    public ProductAdapter(ProductRepository repository, ProductEntityMapper entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = repository.save(entityMapper.toNewEntity(product));
        return entityMapper.toDomain(entity);
    }

    @Override
    public Product update(Product product) {
        ProductEntity entity = repository.findById(product.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        entityMapper.mapToExistingEntity(product, entity);

        ProductEntity saved = repository.save(entity);

        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(ProductEntityMapper::toDomain);
    }

    @Override
    public Page<Product> search(
            String query,
            ProductStatus status,
            int page,
            int size
    ) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));

        return repository.search(query, status, pageable)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        var existent = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
        repository.deleteById(existent.getId());
    }

    @Override
    public boolean existsByName(String name) {
        Optional<ProductEntity> product = repository.findByName(name);
        return product.isPresent();
    }

    @Override
    public boolean existsById(Long id) {
        Optional<ProductEntity> product = repository.findById(id);
        return product.isPresent();
    }
}
