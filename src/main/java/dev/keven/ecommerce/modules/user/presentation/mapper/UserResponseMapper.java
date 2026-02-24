package dev.keven.ecommerce.modules.user.presentation.mapper;

import dev.keven.ecommerce.modules.user.application.result.GetUserResult;
import dev.keven.ecommerce.modules.user.application.result.GetUsersResult;
import dev.keven.ecommerce.modules.user.application.result.UpdateUserRolesResult;
import dev.keven.ecommerce.modules.user.application.result.UserLoginResult;
import dev.keven.ecommerce.modules.user.application.result.UserRegisterResult;
import dev.keven.ecommerce.modules.user.presentation.dto.response.GetUserResponse;
import dev.keven.ecommerce.modules.user.presentation.dto.response.GetUsersResponse;
import dev.keven.ecommerce.modules.user.presentation.dto.response.UpdateUserRolesResponse;
import dev.keven.ecommerce.modules.user.presentation.dto.response.UserLoginResponse;
import dev.keven.ecommerce.modules.user.presentation.dto.response.UserRegisterResponse;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public static UserRegisterResponse toResponse(UserRegisterResult result) {
        return new UserRegisterResponse(
                result.firstName(),
                result.lastName(),
                result.email()
        );
    }

    public static UserLoginResponse toResponse(UserLoginResult result) {
        return new UserLoginResponse(
                result.token(),
                result.refreshToken()
        );
    }

    public static GetUsersResponse toResponse(GetUsersResult result) {
        return new GetUsersResponse(
                result.content().stream()
                        .map(user -> new GetUsersResponse.UserSummaryResponse(
                                user.userId(),
                                user.firstName(),
                                user.lastName(),
                                user.email(),
                                user.roles()
                        ))
                        .toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.first(),
                result.last()
        );
    }

    public static GetUserResponse toResponse(GetUserResult result) {
        return new GetUserResponse(
                result.userId(),
                result.firstName(),
                result.lastName(),
                result.email(),
                result.roles()
        );
    }

    public static UpdateUserRolesResponse toResponse(UpdateUserRolesResult result) {
        return new UpdateUserRolesResponse(
                result.userId(),
                result.roles()
        );
    }
}
