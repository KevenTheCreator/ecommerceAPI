package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.order.application.command.ConfirmOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.ConfirmOrderResult;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderItem;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfirmOrderUseCaseTest {

    @Test
    void executeShouldThrowWhenOrderDoesNotExist() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(orderGateway, productGateway);

        assertThrows(OrderNotFoundException.class, () -> useCase.execute(new ConfirmOrderCommand(1L, 10L)));

        assertEquals(0, productGateway.findCalls);
        assertEquals(0, productGateway.updateCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenProductDoesNotExist() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(orderGateway, productGateway);

        orderGateway.orderByIdAndUserId = Optional.of(createdOrder(
                10L,
                1L,
                List.of(new OrderItem(1L, 100L, 2, new BigDecimal("10.00"))),
                new BigDecimal("20.00")
        ));

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(new ConfirmOrderCommand(1L, 10L)));

        assertEquals(1, productGateway.findCalls);
        assertEquals(0, productGateway.updateCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenProductIsInactive() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(orderGateway, productGateway);

        orderGateway.orderByIdAndUserId = Optional.of(createdOrder(
                10L,
                1L,
                List.of(new OrderItem(1L, 100L, 2, new BigDecimal("10.00"))),
                new BigDecimal("20.00")
        ));
        Product inactiveProduct = activeProduct(100L, 10);
        inactiveProduct.setStatus(ProductStatus.DEACTIVATED);
        productGateway.products.put(100L, inactiveProduct);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(new ConfirmOrderCommand(1L, 10L)));

        assertEquals(1, productGateway.findCalls);
        assertEquals(0, productGateway.updateCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenStockIsInsufficient() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(orderGateway, productGateway);

        orderGateway.orderByIdAndUserId = Optional.of(createdOrder(
                10L,
                1L,
                List.of(new OrderItem(1L, 100L, 3, new BigDecimal("10.00"))),
                new BigDecimal("30.00")
        ));
        productGateway.products.put(100L, activeProduct(100L, 2));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(new ConfirmOrderCommand(1L, 10L)));

        assertEquals(1, productGateway.findCalls);
        assertEquals(0, productGateway.updateCalls);
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldConfirmOrderAndDecreaseStock() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        FakeProductGateway productGateway = new FakeProductGateway();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(orderGateway, productGateway);

        orderGateway.orderByIdAndUserId = Optional.of(createdOrder(
                10L,
                1L,
                List.of(
                        new OrderItem(1L, 100L, 2, new BigDecimal("10.00")),
                        new OrderItem(2L, 200L, 1, new BigDecimal("5.50"))
                ),
                new BigDecimal("25.50")
        ));
        Product firstProduct = activeProduct(100L, 10);
        Product secondProduct = activeProduct(200L, 2);
        productGateway.products.put(100L, firstProduct);
        productGateway.products.put(200L, secondProduct);

        ConfirmOrderResult result = useCase.execute(new ConfirmOrderCommand(1L, 10L));

        assertEquals(10L, result.orderId());
        assertEquals("CONFIRMED", result.status());
        assertEquals(2, result.totalItems());
        assertEquals("25.50", result.totalPrice());
        assertEquals(8, firstProduct.getStock());
        assertEquals(1, secondProduct.getStock());
        assertNotNull(firstProduct.getUpdatedAt());
        assertNotNull(secondProduct.getUpdatedAt());
        assertEquals(2, productGateway.updateCalls);
        assertEquals(1, orderGateway.saveCalls);
    }

    private static Order createdOrder(Long orderId, Long userId, List<OrderItem> items, BigDecimal totalPrice) {
        return Order.rebuild(
                orderId,
                userId,
                OrderStatus.CREATED,
                totalPrice,
                items,
                LocalDateTime.now().minusDays(1)
        );
    }

    private static Product activeProduct(Long productId, int stock) {
        Product product = new Product();
        product.setId(productId);
        product.setStock(stock);
        product.setStatus(ProductStatus.ACTIVE);
        product.setPrice(new BigDecimal("10.00"));
        return product;
    }

    private static final class FakeOrderGateway implements OrderGateway {
        private Optional<Order> orderByIdAndUserId = Optional.empty();
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
            return orderByIdAndUserId;
        }

        @Override
        public Optional<Order> findByUserIdAndStatus(Long userId, OrderStatus status) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public Page<Order> searchByUser(Long userId, OrderStatus status, LocalDate from, LocalDate to, int page, int size) {
            throw new UnsupportedOperationException("not needed in this test");
        }
    }

    private static final class FakeProductGateway implements ProductGateway {
        private final Map<Long, Product> products = new HashMap<>();
        private final List<Product> updatedProducts = new ArrayList<>();
        private int findCalls = 0;
        private int updateCalls = 0;

        @Override
        public Product save(Product product) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public Product update(Product product) {
            updateCalls++;
            updatedProducts.add(product);
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
