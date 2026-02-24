package dev.keven.ecommerce.modules.user.infrastructure.persistence.repository;

import dev.keven.ecommerce.modules.user.infrastructure.persistence.entity.UserEntity;
import dev.keven.ecommerce.modules.user.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
            SELECT DISTINCT u
            FROM UserEntity u
            LEFT JOIN u.roles userRole
            WHERE (:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
              AND (:role IS NULL OR userRole = :role)
            """)
    Page<UserEntity> search(
            @Param("email") String email,
            @Param("role") UserRole role,
            Pageable pageable
    );
}
