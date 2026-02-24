package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.common.exception.UserNotFoundException;
import dev.keven.ecommerce.modules.user.application.command.GetUserByIdCommand;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.application.result.GetUserResult;

public class GetUserByIdUseCase {

    private final UserGateway userGateway;

    public GetUserByIdUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public GetUserResult execute(GetUserByIdCommand command) {
        var user = userGateway.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return GetUserResult.from(user);
    }
}
