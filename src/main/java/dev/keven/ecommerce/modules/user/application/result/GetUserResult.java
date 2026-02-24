package dev.keven.ecommerce.modules.user.application.result;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;

import java.util.Set;

public record GetUserResult(
        Long userId,
        String firstName,
        String lastName,
        String email,
        Set<UserRole> roles
) {

    public static GetUserResult from(User user) {
        return new GetUserResult(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                Set.copyOf(user.getRoles())
        );
    }
}
