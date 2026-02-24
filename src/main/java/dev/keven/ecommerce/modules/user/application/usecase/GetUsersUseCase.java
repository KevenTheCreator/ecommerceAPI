package dev.keven.ecommerce.modules.user.application.usecase;

import dev.keven.ecommerce.modules.user.application.command.GetUsersCommand;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.application.result.GetUsersResult;

public class GetUsersUseCase {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final UserGateway userGateway;

    public GetUsersUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public GetUsersResult execute(GetUsersCommand command) {
        int page = command.page() == null ? DEFAULT_PAGE : Math.max(command.page(), 0);
        int requestedSize = command.size() == null ? DEFAULT_SIZE : command.size();

        if (requestedSize <= 0) {
            throw new IllegalArgumentException("size must be greater than zero");
        }

        int size = Math.min(requestedSize, MAX_PAGE_SIZE);

        var users = userGateway.search(
                command.email(),
                command.role(),
                page,
                size
        );

        return GetUsersResult.from(users);
    }
}
