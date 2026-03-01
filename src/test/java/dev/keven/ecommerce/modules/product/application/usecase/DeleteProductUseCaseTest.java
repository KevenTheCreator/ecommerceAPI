package dev.keven.ecommerce.modules.product.application.usecase;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.product.application.command.DeleteProductCommand;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeleteProductUseCaseTest {

    @Test
    void executeShouldThrowWhenProductDoesNotExist() {
        FakeProductGateway productGateway = new FakeProductGateway();
        DeleteProductUseCase useCase = new DeleteProductUseCase(productGateway);

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(new DeleteProductCommand(1L)));
        assertEquals(0, productGateway.updateCalls);
        assertEquals(1, productGateway.findCalls);
    }

    @Test
    void executeShouldDeactivateWhenProductIsActive() {
        FakeProductGateway productGateway = new FakeProductGateway();
        DeleteProductUseCase useCase = new DeleteProductUseCase(productGateway);

        Product product = buildProduct(10L, ProductStatus.ACTIVE);
        productGateway.products.put(10L, product);

        useCase.execute(new DeleteProductCommand(10L));

        assertEquals(ProductStatus.DEACTIVATED, product.getStatus());
        assertNotNull(product.getUpdatedAt());
        assertEquals(1, productGateway.updateCalls);
        assertEquals(1, productGateway.findCalls);
    }

    @Test
    void executeShouldNotUpdateWhenProductIsAlreadyDeactivated() {
        FakeProductGateway productGateway = new FakeProductGateway();
        DeleteProductUseCase useCase = new DeleteProductUseCase(productGateway);

        Product product = buildProduct(10L, ProductStatus.DEACTIVATED);
        productGateway.products.put(10L, product);

        useCase.execute(new DeleteProductCommand(10L));

        assertEquals(0, productGateway.updateCalls);
        assertEquals(1, productGateway.findCalls);
    }

    private static Product buildProduct(Long id, ProductStatus status) {
        Product product = new Product();
        product.setId(id);
        product.setName("book");
        product.setDescription("desc");
        product.setPrice(new BigDecimal("10.00"));
        product.setStock(5);
        product.setStatus(status);
        product.setCreatedAt(LocalDateTime.now().minusDays(1));
        return product;
    }

    private static final class FakeProductGateway implements ProductGateway {
        private final Map<Long, Product> products = new HashMap<>();
        private int updateCalls = 0;
        private int findCalls = 0;

        @Override
        public Product save(Product product) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public Product update(Product product) {
            updateCalls++;
            products.put(product.getId(), product);
            return product;
        }

        @Override
        public Optional<Product> findById(Long id) {
            findCalls++;
            return Optional.ofNullable(products.get(id));
        }

        @Override
        public Page<Product> search(String query, ProductStatus status, int page, int size) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public void deleteById(Long id) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public boolean existsByName(String name) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public boolean existsById(Long id) {
            throw new UnsupportedOperationException("not needed in this test");
        }
    }
}
