package dev.keven.ecommerce.modules.user.application.command;

import dev.keven.ecommerce.modules.user.domain.UserRole;
import java.util.Set;

public record UserRegisterCommand(
        String firstName,
        String lastName,
        String email,
        String password,
        Set<UserRole> roles
) {
}
