package com.slotsync.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un cliente
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customer_email", columnList = "email"),
    @Index(name = "idx_customer_phone", columnList = "phone_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender; // M, F, Other

    @Column(columnDefinition = "TEXT")
    private String notes; // Notas internas sobre el cliente

    @Column(columnDefinition = "TEXT")
    private String preferences; // Preferencias del cliente (JSON)

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;

    @Column(name = "total_no_shows")
    private Integer totalNoShows = 0;

    @Column(name = "total_cancellations")
    private Integer totalCancellations = 0;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @Column(name = "is_vip")
    private Boolean isVip = false;

    @Column(name = "allow_marketing_emails")
    private Boolean allowMarketingEmails = true;

    @Column(name = "allow_marketing_sms")
    private Boolean allowMarketingSms = false;

    // Relación opcional con User (para clientes registrados)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Obtener nombre completo
     */
    @Transient
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return firstName != null ? firstName : email;
    }

    /**
     * Incrementar contador de reservas
     */
    public void incrementBookings() {
        this.totalBookings = (this.totalBookings != null ? this.totalBookings : 0) + 1;
    }

    /**
     * Incrementar no-shows
     */
    public void incrementNoShows() {
        this.totalNoShows = (this.totalNoShows != null ? this.totalNoShows : 0) + 1;
    }

    /**
     * Incrementar cancelaciones
     */
    public void incrementCancellations() {
        this.totalCancellations = (this.totalCancellations != null ? this.totalCancellations : 0) + 1;
    }

    /**
     * Calcular tasa de no-shows
     */
    @Transient
    public Double getNoShowRate() {
        if (totalBookings == null || totalBookings == 0) return 0.0;
        return (totalNoShows != null ? totalNoShows.doubleValue() : 0.0) / totalBookings * 100;
    }

    /**
     * Añadir puntos de lealtad
     */
    public void addLoyaltyPoints(Integer points) {
        this.loyaltyPoints = (this.loyaltyPoints != null ? this.loyaltyPoints : 0) + points;
    }
}
