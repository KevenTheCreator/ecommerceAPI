package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.modules.cart.application.command.GetCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetCartUseCaseTest {

    @Test
    void executeShouldMarkItemAsUnavailableWhenProductIsDeactivated() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        GetCartUseCase useCase = new GetCartUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);
        productGateway.products.put(10L, product(10L, ProductStatus.DEACTIVATED));

        CartResult result = useCase.execute(new GetCartCommand(1L));

        assertFalse(result.items().getFirst().available());
        assertEquals("PRODUCT_DEACTIVATED", result.items().getFirst().unavailableReason());
        assertEquals(1, productGateway.findCalls);
    }

    @Test
    void executeShouldMarkItemAsUnavailableWhenProductDoesNotExist() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        GetCartUseCase useCase = new GetCartUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        CartResult result = useCase.execute(new GetCartCommand(1L));

        assertFalse(result.items().getFirst().available());
        assertEquals("PRODUCT_NOT_FOUND", result.items().getFirst().unavailableReason());
        assertEquals(1, productGateway.findCalls);
    }

    @Test
    void executeShouldMarkItemAsAvailableWhenProductIsActive() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        GetCartUseCase useCase = new GetCartUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);
        productGateway.products.put(10L, product(10L, ProductStatus.ACTIVE));

        CartResult result = useCase.execute(new GetCartCommand(1L));

        assertTrue(result.items().getFirst().available());
        assertNull(result.items().getFirst().unavailableReason());
        assertEquals(1, productGateway.findCalls);
    }

    private static Product product(Long id, ProductStatus status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        product.setStock(5);
        product.setPrice(new BigDecimal("10.00"));
        return product;
    }

    private static final class FakeOrderGateway implements OrderGateway {
        private Optional<Order> activeCart = Optional.empty();

        @Override
        public Order save(Order order) {
            return order;
        }

        @Override
        public Optional<Order> findById(Long id) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public Optional<Order> findByIdAndUserId(Long id, Long userId) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public Optional<Order> findByUserIdAndStatus(Long userId, OrderStatus status) {
            return activeCart;
        }

        @Override
        public Page<Order> searchByUser(Long userId, OrderStatus status, LocalDate from, LocalDate to, int page, int size) {
            throw new UnsupportedOperationException("not needed in this test");
        }
    }

    private static final class FakeProductGateway implements ProductGateway {
        private final Map<Long, Product> products = new HashMap<>();
        private int findCalls = 0;

        @Override
        public Product save(Product product) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public Product update(Product product) {
            throw new UnsupportedOperationException("not needed in this test");
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
