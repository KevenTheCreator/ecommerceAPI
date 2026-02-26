package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.CartItemNotFoundException;
import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.RemoveItemFromCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RemoveItemFromCartUseCaseTest {

    @Test
    void executeShouldThrowWhenActiveCartDoesNotExist() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        RemoveItemFromCartUseCase useCase = new RemoveItemFromCartUseCase(orderGateway);

        assertThrows(OrderNotFoundException.class, () -> useCase.execute(new RemoveItemFromCartCommand(1L, 10L)));
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldThrowWhenItemDoesNotExistInCart() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        RemoveItemFromCartUseCase useCase = new RemoveItemFromCartUseCase(orderGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 1, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        assertThrows(CartItemNotFoundException.class, () -> useCase.execute(new RemoveItemFromCartCommand(1L, 99L)));
        assertEquals(0, orderGateway.saveCalls);
    }

    @Test
    void executeShouldRemoveLineWhenItemExists() {
        FakeOrderGateway orderGateway = new FakeOrderGateway();
        RemoveItemFromCartUseCase useCase = new RemoveItemFromCartUseCase(orderGateway);

        Order cart = new Order(1L);
        cart.addItem(10L, 1, new BigDecimal("4.00"));
        orderGateway.activeCart = Optional.of(cart);

        CartResult result = useCase.execute(new RemoveItemFromCartCommand(1L, 10L));

        assertEquals(0, result.totalQuantity());
        assertEquals(0, result.items().size());
        assertEquals(BigDecimal.ZERO, result.totalPrice());
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
}
