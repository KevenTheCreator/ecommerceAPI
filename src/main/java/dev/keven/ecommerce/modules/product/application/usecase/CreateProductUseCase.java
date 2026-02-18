package dev.keven.ecommerce.modules.product.application.usecase;

import dev.keven.ecommerce.common.exception.ProductAlreadyExistsException;
import dev.keven.ecommerce.modules.product.application.command.CreateProductCommand;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.application.result.CreateProductResult;
import dev.keven.ecommerce.modules.product.domain.Product;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class CreateProductUseCase {

    private final ProductGateway productGateway;

    public CreateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional
    public CreateProductResult execute(CreateProductCommand command) {
        if (productGateway.existsByName(command.name())) {
            throw new ProductAlreadyExistsException("Product already exists with this name");
        }

        Product product = new  Product();
        product.setName(command.name());
        product.setDescription(command.description());
        product.setPrice(command.price());
        product.setStock(command.stock());
        product.setStatus(command.status());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productGateway.save(product);

        return new CreateProductResult(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status()
        );
    }
}
