package com.slotsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un servicio ofrecido por el negocio
 */
@Entity
@Table(name = "services", indexes = {
    @Index(name = "idx_service_business", columnList = "business_id"),
    @Index(name = "idx_service_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service extends BaseEntity {

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser positiva")
    @Column(nullable = false)
    private Integer duration; // Duración en minutos

    @Size(max = 100)
    @Column(length = 100)
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "color_hex", length = 7)
    private String colorHex = "#6366f1";

    @Column(name = "max_concurrent_bookings")
    private Integer maxConcurrentBookings = 1;

    @Column(name = "buffer_time_before")
    private Integer bufferTimeBefore = 0; // Tiempo de preparación en minutos

    @Column(name = "buffer_time_after")
    private Integer bufferTimeAfter = 0; // Tiempo de limpieza en minutos

    @Column(name = "requires_deposit")
    private Boolean requiresDeposit = false;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToMany(mappedBy = "services")
    @Builder.Default
    private List<Professional> professionals = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Calcular duración total incluyendo buffers
     */
    @Transient
    public Integer getTotalDuration() {
        return duration + bufferTimeBefore + bufferTimeAfter;
    }

    /**
     * Obtener precio formateado
     */
    @Transient
    public String getFormattedPrice() {
        return String.format("€%.2f", price);
    }
}
