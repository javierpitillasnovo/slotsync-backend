package com.slotsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una ubicación física del negocio
 */
@Entity
@Table(name = "locations", indexes = {
    @Index(name = "idx_location_business", columnList = "business_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location extends BaseEntity {

    @NotBlank(message = "El nombre de la ubicación es obligatorio")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;

    @Size(max = 255)
    @Column(length = 255)
    private String address;

    @Size(max = 100)
    @Column(length = 100)
    private String city;

    @Size(max = 100)
    @Column(length = 100)
    private String state;

    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Size(max = 50)
    @Column(length = 50)
    private String country;

    @Column(precision = 10, scale = 7)
    private Double latitude;

    @Column(precision = 10, scale = 7)
    private Double longitude;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 255)
    @Column(length = 255)
    private String email;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    // Horarios de apertura (JSON string)
    @Column(name = "opening_hours", columnDefinition = "TEXT")
    private String openingHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @Transient
    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, postalCode);
    }
}
