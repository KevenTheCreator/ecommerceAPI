package dev.keven.ecommerce.security.auth;

import dev.keven.ecommerce.common.exception.UserNotFoundException;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    private final UserGateway userGateway;

    public AuthenticatedUserService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("authenticated user not found");
        }

        String email = authentication.getName();

        return userGateway.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new UserNotFoundException("authenticated user not found"));
    }
}
