package dev.keven.ecommerce.modules.user.presentation.dto.response;

import dev.keven.ecommerce.modules.user.domain.UserRole;

import java.util.List;
import java.util.Set;

public record GetUsersResponse(
        List<UserSummaryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public record UserSummaryResponse(
            Long userId,
            String firstName,
            String lastName,
            String email,
            Set<UserRole> roles
    ) {
    }
}
