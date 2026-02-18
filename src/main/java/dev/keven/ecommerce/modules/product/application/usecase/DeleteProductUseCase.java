package dev.keven.ecommerce.modules.product.application.usecase;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.product.application.command.DeleteProductCommand;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import org.springframework.transaction.annotation.Transactional;

public class DeleteProductUseCase {
    private final ProductGateway productGateway;

    public DeleteProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional
    public void execute(DeleteProductCommand command) {
        var product = productGateway.findById(command.productId());
        if (product.isEmpty()) throw new ProductNotFoundException("Product not found");
        productGateway.deleteById(command.productId());
    }
}
