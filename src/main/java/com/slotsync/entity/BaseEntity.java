package com.slotsync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Clase base para todas las entidades del sistema
 * Proporciona campos comunes como ID, timestamps y soft delete
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Soft delete - marcar como eliminado sin borrar de la base de datos
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.isActive = false;
    }

    /**
     * Restaurar un registro eliminado
     */
    public void restore() {
        this.deletedAt = null;
        this.isActive = true;
    }

    /**
     * Verificar si el registro está eliminado
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
