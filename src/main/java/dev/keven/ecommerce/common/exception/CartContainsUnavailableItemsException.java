package dev.keven.ecommerce.common.exception;

public class CartContainsUnavailableItemsException extends RuntimeException {
    public CartContainsUnavailableItemsException(String message) {
        super(message);
    }
}
