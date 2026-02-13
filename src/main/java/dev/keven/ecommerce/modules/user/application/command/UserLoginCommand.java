package dev.keven.ecommerce.modules.user.application.command;

public record UserLoginCommand(
        String email,
        String password
) {
}
