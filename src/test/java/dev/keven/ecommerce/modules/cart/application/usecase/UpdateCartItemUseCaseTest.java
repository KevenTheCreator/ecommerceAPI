package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.CartItemNotFoundException;
import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.UpdateCartItemCommand;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateCartItemUseCaseTest {

    @Test
    void executeShouldThrowWhenActiveCartDoesNotExist() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        assertThrows(OrderNotFoundException.class, () -> useCase.execute(new UpdateCartItemCommand(1L, 10L, 1)));

        assertEquals(0, productGateway.findCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldRemoveLineWhenQuantityIsZero() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        CartResult result = useCase.execute(new UpdateCartItemCommand(1L, 10L, 0));

        assertEquals(0, result.totalQuantity());
        assertEquals(0, result.items().size());
        assertEquals(BigDecimal.ZERO, result.totalPrice());
        assertEquals(0, productGateway.findCalls);
        assertEquals(1, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenQuantityIsZeroAndItemDoesNotExist() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        assertThrows(CartItemNotFoundException.class, () -> useCase.execute(new UpdateCartItemCommand(1L, 99L, 0)));

        assertEquals(0, productGateway.findCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenProductDoesNotExistForPositiveQuantity() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(new UpdateCartItemCommand(1L, 10L, 3)));

        assertEquals(1, productGateway.findCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenProductStockIsInsufficient() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        Product product = new Product();
        product.setId(10L);
        product.setStatus(ProductStatus.ACTIVE);
        product.setStock(1);
        product.setPrice(new BigDecimal("4.00"));
        productGateway.products.put(10L, product);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(new UpdateCartItemCommand(1L, 10L, 3)));

        assertEquals(1, productGateway.findCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenTryingToUpdateAnItemThatIsNotInCart() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(99L, 1, new BigDecimal("3.00"));
        orderGateway.activeCart = Optional.of(cart);

        Product product = new Product();
        product.setId(10L);
        product.setStatus(ProductStatus.ACTIVE);
        product.setStock(10);
        product.setPrice(new BigDecimal("4.00"));
        productGateway.products.put(10L, product);

        assertThrows(CartItemNotFoundException.class, () -> useCase.execute(new UpdateCartItemCommand(1L, 10L, 2)));

        assertEquals(1, productGateway.findCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldUpdateQuantityWhenDataIsValid() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        UpdateCartItemUseCase useCase = new UpdateCartItemUseCase(orderGateway, productGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 2, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        Product product = new Product();
        product.setId(10L);
        product.setStatus(ProductStatus.ACTIVE);
        product.setStock(10);
        product.setPrice(new BigDecimal("5.00"));
        productGateway.products.put(10L, product);

        CartResult result = useCase.execute(new UpdateCartItemCommand(1L, 10L, 3));

        assertEquals(3, result.totalQuantity());
        assertEquals(1, result.items().size());
        assertEquals(new BigDecimal("15.00"), result.totalPrice());
        assertEquals(3, result.items().getFirst().quantity());
        assertEquals(new BigDecimal("5.00"), result.items().getFirst().unitPrice());
        assertEquals(1, productGateway.findCalls);
        assertEquals(1, orderGateway.saveCalls);
    }

    private static final class FakeOrderGateway implements OrderGateway {
        private Optional<Order> activeCart = Optional.empty();
        private int saveCalls = 0;

        @Override
        public Order save(Order order) {
            saveCalls++;
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
