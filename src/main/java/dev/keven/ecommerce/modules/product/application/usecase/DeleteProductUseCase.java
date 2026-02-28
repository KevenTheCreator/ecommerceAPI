package dev.keven.ecommerce.modules.product.application.usecase;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.product.application.command.DeleteProductCommand;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class DeleteProductUseCase {
    private final ProductGateway productGateway;

    public DeleteProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional
    public void execute(DeleteProductCommand command) {
        var product = productGateway.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStatus() == ProductStatus.DEACTIVATED) {
            return;
        }

        product.setStatus(ProductStatus.DEACTIVATED);
        product.setUpdatedAt(LocalDateTime.now());
        productGateway.update(product);
    }
}
