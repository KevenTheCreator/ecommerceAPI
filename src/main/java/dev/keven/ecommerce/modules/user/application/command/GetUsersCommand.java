package dev.keven.ecommerce.modules.user.application.command;

import dev.keven.ecommerce.modules.user.domain.UserRole;

public record GetUsersCommand(
        String email,
        UserRole role,
        Integer page,
        Integer size
) {
}
