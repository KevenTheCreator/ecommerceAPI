package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.common.exception.UserAlreadyExistsException;
import dev.keven.ecommerce.modules.user.application.command.UserRegisterCommand;
import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.application.result.UserRegisterResult;
import dev.keven.ecommerce.security.hash.PasswordHashService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public class UserRegisterUseCase {

    private final UserGateway userGateway;
    private final PasswordHashService passwordHashService;

    public UserRegisterUseCase(UserGateway userGateway, PasswordHashService passwordHashService) {
        this.userGateway = userGateway;
        this.passwordHashService = passwordHashService;
    }

    @Transactional
    public UserRegisterResult execute(UserRegisterCommand command) {
        if (userGateway.findByEmail(command.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        String encodedPassword = passwordHashService.hash(command.password());
        User user = new User();

        user.setFirstName(command.firstName());
        user.setLastName(command.lastName());
        user.setEmail(command.email());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(UserRole.CUSTOMER));

        userGateway.save(user);

        return new UserRegisterResult(user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
