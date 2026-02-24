package dev.keven.ecommerce.modules.user.presentation.dto.request;

import dev.keven.ecommerce.modules.user.domain.UserRole;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdateUserRolesRequest(
        @NotEmpty Set<UserRole> roles
) {
}
