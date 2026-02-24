package dev.keven.ecommerce.modules.user.infrastructure.adapter;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.infrastructure.persistence.entity.UserEntity;
import dev.keven.ecommerce.modules.user.infrastructure.persistence.repository.UserRepository;
import dev.keven.ecommerce.modules.user.presentation.mapper.UserEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class UserAdapter implements UserGateway {

    private final UserRepository userRepository;

    private final UserEntityMapper entityMapper;

    public UserAdapter(UserRepository userRepository, UserEntityMapper entityMapper) {
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public Page<User> search(
            String email,
            UserRole role,
            int page,
            int size
    ) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        return userRepository.search(email, role, pageable)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userRepository.save(entityMapper.toEntity(user));
        return UserEntityMapper.toDomain(entity);
    }
}
