package dev.keven.ecommerce.modules.user.application.gateway;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserGateway {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Page<User> search(
            String email,
            UserRole role,
            int page,
            int size
    );
    User save(User user);
}
