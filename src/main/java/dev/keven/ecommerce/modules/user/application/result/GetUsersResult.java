package dev.keven.ecommerce.modules.user.application.result;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public record GetUsersResult(
        List<UserSummaryResult> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static GetUsersResult from(Page<User> users) {
        return new GetUsersResult(
                users.getContent().stream().map(UserSummaryResult::from).toList(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }

    public record UserSummaryResult(
            Long userId,
            String firstName,
            String lastName,
            String email,
            Set<UserRole> roles
    ) {
        public static UserSummaryResult from(User user) {
            return new UserSummaryResult(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    Set.copyOf(user.getRoles())
            );
        }
    }
}
