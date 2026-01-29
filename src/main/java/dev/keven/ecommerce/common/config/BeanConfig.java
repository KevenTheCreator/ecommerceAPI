package dev.keven.ecommerce.common.config;

import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.application.usecase.CreateProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.DeleteProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.GetProductByIdUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.UpdateProductUseCase;
import dev.keven.ecommerce.modules.product.infrastructure.adapter.ProductAdapter;
import dev.keven.ecommerce.modules.product.infrastructure.persistence.repository.ProductRepository;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductEntityMapper;
import dev.keven.ecommerce.modules.user.application.usecase.UserLoginUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.UserRegisterUseCase;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.infrastructure.adapter.UserAdapter;
import dev.keven.ecommerce.modules.user.infrastructure.persistence.repository.UserRepository;
import dev.keven.ecommerce.modules.user.presentation.mapper.UserEntityMapper;
import dev.keven.ecommerce.security.auth.AuthService;
import dev.keven.ecommerce.security.hash.PasswordHashService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    UserRegisterUseCase userRegisterUseCase(UserGateway userGateway, PasswordHashService passwordHashService) {
        return new UserRegisterUseCase(userGateway, passwordHashService);
    }

    @Bean
    UserLoginUseCase userLoginUseCase(UserGateway userGateway, PasswordHashService passwordHashService, AuthService authService) {
        return new UserLoginUseCase(userGateway, passwordHashService, authService);
    }

    @Bean
    CreateProductUseCase createProductUseCase(ProductGateway productGateway) {
        return new CreateProductUseCase(productGateway);
    }

    @Bean
    DeleteProductUseCase deleteProductUseCase(ProductGateway productGateway) {
        return new DeleteProductUseCase(productGateway);
    }

    @Bean
    GetProductByIdUseCase getProductByIdUseCase(ProductGateway productGateway) {
        return new GetProductByIdUseCase(productGateway);
    }

    @Bean
    UpdateProductUseCase updateProductUseCase(ProductGateway productGateway) {
        return new UpdateProductUseCase(productGateway);
    }

    @Bean
    ProductGateway productGateway(ProductRepository repository, ProductEntityMapper entityMapper) {
        return new ProductAdapter(repository, entityMapper);
    }

    @Bean
    PasswordHashService passwordHashService(BCryptPasswordEncoder encoder) {
        return new PasswordHashService(encoder);
    }

    @Bean
    UserGateway userGateway(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        return new UserAdapter(userRepository, userEntityMapper);
    }
}
