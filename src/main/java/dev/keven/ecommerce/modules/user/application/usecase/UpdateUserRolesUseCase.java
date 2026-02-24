package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.common.exception.UserNotFoundException;
import dev.keven.ecommerce.modules.user.application.command.UpdateUserRolesCommand;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.application.result.UpdateUserRolesResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public class UpdateUserRolesUseCase {

    private final UserGateway userGateway;

    public UpdateUserRolesUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Transactional
    public UpdateUserRolesResult execute(UpdateUserRolesCommand command) {
        if (command.roles() == null || command.roles().isEmpty()) {
            throw new IllegalArgumentException("roles cannot be empty");
        }

        var user = userGateway.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setRoles(Set.copyOf(command.roles()));

        var updated = userGateway.save(user);
        return UpdateUserRolesResult.from(updated);
    }
}
