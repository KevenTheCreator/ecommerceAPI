package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.common.exception.PasswordValidationException;
import dev.keven.ecommerce.common.exception.UserNotFoundException;
import dev.keven.ecommerce.modules.user.application.command.UserLoginCommand;
import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.application.result.UserLoginResult;
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

    public UserLoginResult execute(UserLoginCommand command) {
        User user = userGateway.findByEmail(command.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordHashService.matches(command.password(), user.getPassword())) {
            throw new PasswordValidationException("Password doesn't match");
        }

        String token = authService.generateToken(user);
        String refreshToken = authService.refreshToken(user);

        return new UserLoginResult(token, refreshToken);
    }
}
