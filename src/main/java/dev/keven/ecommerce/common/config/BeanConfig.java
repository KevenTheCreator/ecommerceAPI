package dev.keven.ecommerce.common.config;

import dev.keven.ecommerce.modules.cart.application.usecase.AddItemToCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.CheckoutCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.GetCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.RemoveItemFromCartUseCase;
import dev.keven.ecommerce.modules.cart.application.usecase.UpdateCartItemUseCase;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.usecase.*;
import dev.keven.ecommerce.modules.order.infrastructure.adapter.OrderAdapter;
import dev.keven.ecommerce.modules.order.infrastructure.persistence.repository.OrderRepository;
import dev.keven.ecommerce.modules.order.presentation.mapper.OrderEntityMapper;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.application.usecase.CreateProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.DeleteProductUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.GetProductByIdUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.ListProductsUseCase;
import dev.keven.ecommerce.modules.product.application.usecase.UpdateProductUseCase;
import dev.keven.ecommerce.modules.product.infrastructure.adapter.ProductAdapter;
import dev.keven.ecommerce.modules.product.infrastructure.persistence.repository.ProductRepository;
import dev.keven.ecommerce.modules.product.presentation.mapper.ProductEntityMapper;
import dev.keven.ecommerce.modules.user.application.usecase.GetUserByIdUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.GetUsersUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.RefreshTokenUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.UpdateUserRolesUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.UserLoginUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.UserRegisterUseCase;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.infrastructure.adapter.UserAdapter;
import dev.keven.ecommerce.modules.user.infrastructure.persistence.repository.UserRepository;
import dev.keven.ecommerce.modules.user.presentation.mapper.UserEntityMapper;
import dev.keven.ecommerce.security.auth.AuthService;
import dev.keven.ecommerce.security.hash.PasswordHashService;
import dev.keven.ecommerce.security.jwt.JwtProvider;
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
    ListProductsUseCase listProductsUseCase(ProductGateway productGateway) {
        return new ListProductsUseCase(productGateway);
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

    @Bean
    CancelOrderUseCase cancelOrderUseCase(OrderGateway orderGateway) {
        return new CancelOrderUseCase(orderGateway);
    }

    @Bean
    ConfirmOrderUseCase confirmOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        return new ConfirmOrderUseCase(orderGateway, productGateway);
    }

    @Bean
    GetOrderByIdUseCase getOrderByIdUseCase(OrderGateway orderGateway) {
        return new GetOrderByIdUseCase(orderGateway);
    }

    @Bean
    GetOrdersUseCase getOrdersUseCase(OrderGateway orderGateway) {
        return new GetOrdersUseCase(orderGateway);
    }

    @Bean
    GetCartUseCase getCartUseCase(OrderGateway orderGateway) {
        return new GetCartUseCase(orderGateway);
    }

    @Bean
    AddItemToCartUseCase addItemToCartUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        return new AddItemToCartUseCase(orderGateway, productGateway);
    }

    @Bean
    UpdateCartItemUseCase updateCartItemUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        return new UpdateCartItemUseCase(orderGateway, productGateway);
    }

    @Bean
    RemoveItemFromCartUseCase removeItemFromCartUseCase(OrderGateway orderGateway) {
        return new RemoveItemFromCartUseCase(orderGateway);
    }

    @Bean
    CheckoutCartUseCase checkoutCartUseCase(OrderGateway orderGateway, ConfirmOrderUseCase confirmOrderUseCase) {
        return new CheckoutCartUseCase(orderGateway, confirmOrderUseCase);
    }

    @Bean
    OrderGateway orderGateway(OrderRepository repository, OrderEntityMapper entityMapper) {
        return new OrderAdapter(repository, entityMapper);
    }

    @Bean
    RefreshTokenUseCase refreshTokenUseCase(UserGateway userGateway, JwtProvider jwtProvider) {
        return new RefreshTokenUseCase(userGateway, jwtProvider);
    }

    @Bean
    GetUsersUseCase getUsersUseCase(UserGateway userGateway) {
        return new GetUsersUseCase(userGateway);
    }

    @Bean
    GetUserByIdUseCase getUserByIdUseCase(UserGateway userGateway) {
        return new GetUserByIdUseCase(userGateway);
    }

    @Bean
    UpdateUserRolesUseCase updateUserRolesUseCase(UserGateway userGateway) {
        return new UpdateUserRolesUseCase(userGateway);
    }
}
