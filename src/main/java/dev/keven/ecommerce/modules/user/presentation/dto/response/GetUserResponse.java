package dev.keven.ecommerce.modules.user.presentation.dto.response;

import dev.keven.ecommerce.modules.user.domain.UserRole;

import java.util.Set;

public record GetUserResponse(
        Long userId,
        String firstName,
        String lastName,
        String email,
        Set<UserRole> roles
) {
}
