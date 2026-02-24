package dev.keven.ecommerce.modules.user.presentation.mapper;

import dev.keven.ecommerce.modules.user.application.command.GetUserByIdCommand;
import dev.keven.ecommerce.modules.user.application.command.GetUsersCommand;
import dev.keven.ecommerce.modules.user.application.command.UpdateUserRolesCommand;
import dev.keven.ecommerce.modules.user.application.command.UserLoginCommand;
import dev.keven.ecommerce.modules.user.application.command.UserRegisterCommand;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import dev.keven.ecommerce.modules.user.presentation.dto.request.UserLoginRequest;
import dev.keven.ecommerce.modules.user.presentation.dto.request.UpdateUserRolesRequest;
import dev.keven.ecommerce.modules.user.presentation.dto.request.UserRegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public static UserRegisterCommand toCommand(UserRegisterRequest request) {
        return new UserRegisterCommand(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );
    }

    public static UserLoginCommand toCommand(UserLoginRequest request) {
        return new UserLoginCommand(
                request.email(),
                request.password()
        );
    }

    public static GetUsersCommand toGetUsersCommand(
            String email,
            UserRole role,
            Integer page,
            Integer size
    ) {
        return new GetUsersCommand(
                email,
                role,
                page,
                size
        );
    }

    public static GetUserByIdCommand toGetUserByIdCommand(Long userId) {
        return new GetUserByIdCommand(userId);
    }

    public static UpdateUserRolesCommand toUpdateUserRolesCommand(Long userId, UpdateUserRolesRequest request) {
        return new UpdateUserRolesCommand(userId, request.roles());
    }
}
