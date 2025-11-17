package com.slotsync.repository;

import com.slotsync.entity.User;
import com.slotsync.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Buscar usuario por email
     */
    Optional<User> findByEmail(String email);

    /**
     * Verificar si existe un email
     */
    boolean existsByEmail(String email);

    /**
     * Buscar usuarios por rol
     */
    List<User> findByRole(Role role);

    /**
     * Buscar usuarios por negocio
     */
    List<User> findByBusinessId(Long businessId);

    /**
     * Buscar usuarios por negocio y rol
     */
    List<User> findByBusinessIdAndRole(Long businessId, Role role);

    /**
     * Buscar usuario por email verification token
     */
    Optional<User> findByEmailVerificationToken(String token);

    /**
     * Buscar usuario por password reset token
     */
    Optional<User> findByPasswordResetToken(String token);

    /**
     * Buscar usuarios activos de un negocio
     */
    @Query("SELECT u FROM User u WHERE u.business.id = :businessId AND u.isActive = true")
    List<User> findActiveUsersByBusinessId(@Param("businessId") Long businessId);

    /**
     * Contar usuarios por negocio
     */
    long countByBusinessId(Long businessId);

    /**
     * Buscar por email ignorando mayúsculas
     */
    Optional<User> findByEmailIgnoreCase(String email);
}
