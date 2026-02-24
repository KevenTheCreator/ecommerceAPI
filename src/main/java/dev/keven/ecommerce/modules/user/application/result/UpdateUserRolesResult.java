package dev.keven.ecommerce.modules.user.application.result;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;

import java.util.Set;

public record UpdateUserRolesResult(
        Long userId,
        Set<UserRole> roles
) {

    public static UpdateUserRolesResult from(User user) {
        return new UpdateUserRolesResult(user.getId(), Set.copyOf(user.getRoles()));
    }
}
