package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.common.exception.UserAlreadyExistsException;
import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.presentation.dto.request.UserRegisterRequest;
import dev.keven.ecommerce.modules.user.presentation.dto.response.UserRegisterResponse;
import dev.keven.ecommerce.security.hash.PasswordHashService;

import java.util.Set;

public class UserRegisterUseCase {

    private final UserGateway userGateway;
    private final PasswordHashService passwordHashService;

    public UserRegisterUseCase(UserGateway userGateway, PasswordHashService passwordHashService) {
        this.userGateway = userGateway;
        this.passwordHashService = passwordHashService;
    }

    public UserRegisterResponse execute(UserRegisterRequest request) {
        if (userGateway.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        String encodedPassword = passwordHashService.hash(request.password());
        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(encodedPassword);
        user.setRoles(request.roles() == null || request.roles().isEmpty() ? Set.of(UserRole.CUSTOMER) : request.roles());

        userGateway.save(user);

        return new UserRegisterResponse(request.firstName(), request.lastName(), request.email());
    }
}
