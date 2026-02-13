package dev.keven.ecommerce.modules.user.application.result;

public record UserLoginResult(
        String token,
        String refreshToken
) {
}
