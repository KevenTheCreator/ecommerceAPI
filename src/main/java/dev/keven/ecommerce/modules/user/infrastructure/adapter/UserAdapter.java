package dev.keven.ecommerce.modules.user.infrastructure.adapter;

import dev.keven.ecommerce.modules.user.domain.User;
import dev.keven.ecommerce.modules.user.application.gateway.UserGateway;
import dev.keven.ecommerce.modules.user.infrastructure.persistence.entity.UserEntity;
import dev.keven.ecommerce.modules.user.infrastructure.persistence.repository.UserRepository;
import dev.keven.ecommerce.modules.user.presentation.mapper.UserEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
    public User save(User user) {
        UserEntity entity = userRepository.save(entityMapper.toEntity(user));
        return UserEntityMapper.toDomain(entity);
    }
}
