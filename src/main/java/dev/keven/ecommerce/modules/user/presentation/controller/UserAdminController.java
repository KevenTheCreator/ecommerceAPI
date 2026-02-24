package dev.keven.ecommerce.modules.user.presentation.controller;

import dev.keven.ecommerce.modules.user.application.usecase.GetUserByIdUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.GetUsersUseCase;
import dev.keven.ecommerce.modules.user.application.usecase.UpdateUserRolesUseCase;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import dev.keven.ecommerce.modules.user.presentation.dto.request.UpdateUserRolesRequest;
import dev.keven.ecommerce.modules.user.presentation.dto.response.GetUserResponse;
import dev.keven.ecommerce.modules.user.presentation.dto.response.GetUsersResponse;
import dev.keven.ecommerce.modules.user.presentation.dto.response.UpdateUserRolesResponse;
import dev.keven.ecommerce.modules.user.presentation.mapper.UserRequestMapper;
import dev.keven.ecommerce.modules.user.presentation.mapper.UserResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserAdminController {

    private final GetUsersUseCase getUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserRolesUseCase updateUserRolesUseCase;

    public UserAdminController(
            GetUsersUseCase getUsersUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            UpdateUserRolesUseCase updateUserRolesUseCase
    ) {
        this.getUsersUseCase = getUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.updateUserRolesUseCase = updateUserRolesUseCase;
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserRole role,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        var result = getUsersUseCase.execute(
                UserRequestMapper.toGetUsersCommand(email, role, page, size)
        );

        return ResponseEntity.status(HttpStatus.OK).body(UserResponseMapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id) {
        var result = getUserByIdUseCase.execute(UserRequestMapper.toGetUserByIdCommand(id));
        return ResponseEntity.status(HttpStatus.OK).body(UserResponseMapper.toResponse(result));
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<UpdateUserRolesResponse> updateUserRoles(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRolesRequest request
    ) {
        var result = updateUserRolesUseCase.execute(UserRequestMapper.toUpdateUserRolesCommand(id, request));
        return ResponseEntity.status(HttpStatus.OK).body(UserResponseMapper.toResponse(result));
    }
}
