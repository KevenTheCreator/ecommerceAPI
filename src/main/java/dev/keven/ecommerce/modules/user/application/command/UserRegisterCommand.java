package dev.keven.ecommerce.modules.user.application.command;

public record UserRegisterCommand(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
