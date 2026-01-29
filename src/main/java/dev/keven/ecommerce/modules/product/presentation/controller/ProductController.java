package dev.keven.ecommerce.modules.product.presentation.controller;

import dev.keven.ecommerce.modules.product.application.usecase.CreateProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.DeleteProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.GetProductByIdUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.UpdateProductUseCase;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.presentation.dto.request.CreateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.request.UpdateProductRequest;
import dev.keven.ecommerce.modules.product.presentation.dto.response.CreateProductResponse;
import dev.keven.ecommerce.modules.product.presentation.dto.response.UpdateProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, DeleteProductUseCase deleteProductUseCase, UpdateProductUseCase updateProductUseCase, GetProductByIdUseCase getProductByIdUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        CreateProductResponse response = createProductUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(getProductByIdUseCase.execute(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateProductResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        UpdateProductResponse response = updateProductUseCase.execute(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
