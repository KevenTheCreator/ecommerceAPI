package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.common.exception.PasswordValidationException;
import dev.keven.ecommerce.common.exception.UserNotFoundException;
import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.presentation.dto.request.UserLoginRequest;
import dev.keven.ecommerce.modules.user.presentation.dto.response.UserLoginResponse;
import dev.keven.ecommerce.security.auth.AuthService;
import dev.keven.ecommerce.security.hash.PasswordHashService;

public class UserLoginUseCase {

    private final UserGateway userGateway;
    private final PasswordHashService passwordHashService;
    private final AuthService authService;

    public UserLoginUseCase(UserGateway userGateway, PasswordHashService passwordHashService, AuthService authService) {
        this.userGateway = userGateway;
        this.passwordHashService = passwordHashService;
        this.authService = authService;
    }

    public UserLoginResponse execute(UserLoginRequest request) {
        User user = userGateway.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordHashService.matches(request.password(),  user.getPassword())) {
            throw new PasswordValidationException("Password doesn't match");
        }

        String token = authService.generateToken(user);

        return new UserLoginResponse(token);
    }
}
