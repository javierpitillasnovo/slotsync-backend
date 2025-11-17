package com.slotsync.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un profesional/empleado que ofrece servicios
 */
@Entity
@Table(name = "professionals", indexes = {
    @Index(name = "idx_professional_business", columnList = "business_id"),
    @Index(name = "idx_professional_user", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professional extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 100)
    private String title; // Ej: "Estilista Senior", "Peluquero"

    @Column(length = 500)
    private String specialties; // Especialidades separadas por comas

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "accepts_online_bookings")
    private Boolean acceptsOnlineBookings = true;

    @Column(name = "rating_average")
    private Double ratingAverage = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    // Horario de trabajo (JSON string)
    @Column(name = "working_hours", columnDefinition = "TEXT")
    private String workingHours;

    // Color para el calendario
    @Column(name = "calendar_color", length = 7)
    private String calendarColor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "professional_services",
        joinColumns = @JoinColumn(name = "professional_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Builder.Default
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProfessionalAvailability> availabilities = new ArrayList<>();

    /**
     * Actualizar rating promedio
     */
    public void updateRating(Double newRating) {
        if (totalReviews == 0) {
            this.ratingAverage = newRating;
            this.totalReviews = 1;
        } else {
            double totalScore = ratingAverage * totalReviews;
            totalReviews++;
            this.ratingAverage = (totalScore + newRating) / totalReviews;
        }
    }

    /**
     * Obtener nombre completo del profesional
     */
    @Transient
    public String getFullName() {
        return user != null ? user.getFullName() : "";
    }
}
