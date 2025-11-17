package com.slotsync.entity;

import com.slotsync.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una reserva/cita
 */
@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_business", columnList = "business_id"),
    @Index(name = "idx_booking_customer", columnList = "customer_id"),
    @Index(name = "idx_booking_professional", columnList = "professional_id"),
    @Index(name = "idx_booking_service", columnList = "service_id"),
    @Index(name = "idx_booking_date", columnList = "start_time"),
    @Index(name = "idx_booking_status", columnList = "status"),
    @Index(name = "idx_booking_code", columnList = "booking_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @Column(name = "booking_code", unique = true, nullable = false, length = 20)
    private String bookingCode; // Código único de reserva (ej: BK20240115001)

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discount_code", length = 50)
    private String discountCode;

    @Column(columnDefinition = "TEXT")
    private String notes; // Notas del cliente

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes; // Notas internas del negocio

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancelled_by_user_id")
    private Long cancelledByUserId;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "reminder_sent_at")
    private LocalDateTime reminderSentAt;

    @Column(name = "confirmation_sent_at")
    private LocalDateTime confirmationSentAt;

    @Column(name = "client_showed_up")
    private Boolean clientShowedUp;

    @Column(name = "rating")
    private Integer rating; // Calificación del cliente (1-5)

    @Column(name = "review", columnDefinition = "TEXT")
    private String review; // Comentario del cliente

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    /**
     * Obtener duración en minutos
     */
    @Transient
    public Long getDurationMinutes() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMinutes();
        }
        return 0L;
    }

    /**
     * Verificar si la reserva puede ser cancelada
     */
    @Transient
    public boolean isCancellable(Integer minHoursBefore) {
        if (status == BookingStatus.COMPLETED || status == BookingStatus.CANCELLED) {
            return false;
        }
        LocalDateTime deadline = startTime.minusHours(minHoursBefore);
        return LocalDateTime.now().isBefore(deadline);
    }

    /**
     * Verificar si la reserva está en el pasado
     */
    @Transient
    public boolean isPast() {
        return endTime != null && endTime.isBefore(LocalDateTime.now());
    }

    /**
     * Verificar si la reserva está activa (en curso)
     */
    @Transient
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return startTime != null && endTime != null &&
               now.isAfter(startTime) && now.isBefore(endTime);
    }

    /**
     * Confirmar la reserva
     */
    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    /**
     * Cancelar la reserva
     */
    public void cancel(String reason, Long userId) {
        this.status = BookingStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.cancellationReason = reason;
        this.cancelledByUserId = userId;
    }

    /**
     * Marcar como completada
     */
    public void complete() {
        this.status = BookingStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.clientShowedUp = true;
    }

    /**
     * Marcar como no-show
     */
    public void markAsNoShow() {
        this.status = BookingStatus.NO_SHOW;
        this.clientShowedUp = false;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Añadir calificación
     */
    public void addReview(Integer rating, String review) {
        this.rating = rating;
        this.review = review;
        this.reviewedAt = LocalDateTime.now();
    }
}
