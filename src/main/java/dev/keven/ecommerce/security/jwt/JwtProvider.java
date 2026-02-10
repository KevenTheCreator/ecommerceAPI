package dev.keven.ecommerce.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.keven.ecommerce.common.exception.TokenGenerationException;
import dev.keven.ecommerce.common.exception.TokenValidationException;
import dev.keven.ecommerce.modules.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {

    private final Algorithm algorithm;
    private final Long tokenExpiration;
    private final Long refreshTokenExpiration;

    public JwtProvider(
            @Value("${spring.app.secret}") String secret,
            @Value("${spring.app.token.expiration}") Long tokenExpiration,
            @Value("${spring.app.refresh.expiration}") Long refreshTokenExpiration
    ) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret is not configured");
        }

        this.algorithm = Algorithm.HMAC256(secret);
        this.tokenExpiration = tokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withIssuer("Access")
                    .withIssuedAt(Date.from(Instant.now()))
                    .withSubject(user.getEmail())
                    .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
                    .withExpiresAt(getAccessTokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Error creating JWT Token");
        }
    }

    public DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer("Access")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            throw new TokenValidationException("Token verification failed");
        }
    }

    public String generateRefreshToken(User user) {
        try {
            return JWT.create()
                    .withIssuer("Refresh")
                    .withIssuedAt(new Date())
                    .withSubject(user.getEmail())
                    .withExpiresAt(Date.from(getRefreshTokenExpiration()))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Error creating refresh token");
        }
    }

    public DecodedJWT verifyRefreshToken(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer("Refresh")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    private Instant getAccessTokenExpiration() {
        return Instant.now().plus(Duration.ofHours(tokenExpiration));
    }

    private Instant getRefreshTokenExpiration() { return Instant.now().plus(Duration.ofHours(refreshTokenExpiration)); }
}
