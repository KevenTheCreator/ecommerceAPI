package dev.keven.ecommerce.modules.product.application.usecase;

import dev.keven.ecommerce.common.exception.ProductAlreadyExistsException;
import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.product.application.command.UpdateProductCommand;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.application.result.UpdateProductResult;
import dev.keven.ecommerce.modules.product.domain.Product;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class UpdateProductUseCase {

    private final ProductGateway productGateway;

    public UpdateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional
    public UpdateProductResult execute(UpdateProductCommand command) {
        var existent = productGateway.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (command.name() != null
                && !command.name().equals(existent.getName())
                && productGateway.existsByName(command.name())) {
            throw new ProductAlreadyExistsException(String.format("Product with name '%s' already exists", command.name()));
        }

        var updated = new Product(
                existent.getId(),
                command.name() != null ? command.name() : existent.getName(),
                command.description() != null ? command.description() : existent.getDescription(),
                command.price() != null ? command.price() : existent.getPrice(),
                command.stock() != null ? command.stock() : existent.getStock(),
                command.status() != null ? command.status() : existent.getStatus(),
                existent.getCreatedAt(),
                LocalDateTime.now()
        );

        productGateway.update(updated);

        return new UpdateProductResult(
                updated.getName(),
                updated.getDescription(),
                updated.getPrice(),
                updated.getStock(),
                updated.getStatus(),
                updated.getUpdatedAt()
        );
    }
}
