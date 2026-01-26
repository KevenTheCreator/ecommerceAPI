package dev.keven.ecommerce.modules.user.application.gateway;

import dev.keven.ecommerce.modules.user.domain.User;
import java.util.Optional;

public interface UserGateway {
    Optional<User> findByEmail(String email);
    User save(User user);
}
