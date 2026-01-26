package dev.keven.ecommerce.security.auth;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.security.jwt.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtProvider JwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.JwtProvider = jwtProvider;
    }

    public String generateToken(User user) {
        return JwtProvider.generateToken(user);
    }
}
