package dev.keven.ecommerce.modules.user.application.result;

public record UserRegisterResult(
        String firstName,
        String lastName,
        String email
) {
}
