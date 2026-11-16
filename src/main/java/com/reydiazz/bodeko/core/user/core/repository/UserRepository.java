package com.reydiazz.bodeko.core.user.core.repository;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    long countByRole(com.reydiazz.bodeko.core.user.core.model.enums.UserRole role);
    long countByRoleAndIsActive(com.reydiazz.bodeko.core.user.core.model.enums.UserRole role, boolean active);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id and u.role = :role")
    Optional<User> findByIdAndRoleForUpdate(@Param("id") UUID id, @Param("role") com.reydiazz.bodeko.core.user.core.model.enums.UserRole role);

}
