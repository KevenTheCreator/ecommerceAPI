package dev.keven.ecommerce.modules.user.presentation.dto.response;

import dev.keven.ecommerce.modules.user.domain.UserRole;

import java.util.Set;

public record UpdateUserRolesResponse(
        Long userId,
        Set<UserRole> roles
) {
}
