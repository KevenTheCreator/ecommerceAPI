package dev.keven.ecommerce.modules.product.presentation.dto.request;

import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequest(
     @NotBlank(message = "nome não pode ser vazio") String name,
     @NotBlank(message = "descrição não pode ser vazio") @Size(max = 70) String description,
     @NotNull(message = "preço não pode ser nulo") @Positive BigDecimal price,
     @NotNull(message = "estoque não pode ser nulo") @PositiveOrZero Integer stock,
     @NotNull(message = "status não pode ser nulo") ProductStatus status
) {
}
