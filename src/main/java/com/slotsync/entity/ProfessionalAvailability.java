package com.slotsync.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad que representa la disponibilidad de un profesional
 */
@Entity
@Table(name = "professional_availability", indexes = {
    @Index(name = "idx_availability_professional", columnList = "professional_id"),
    @Index(name = "idx_availability_date", columnList = "specific_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalAvailability extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 10)
    private DayOfWeek dayOfWeek; // Para horarios recurrentes

    @Column(name = "specific_date")
    private LocalDate specificDate; // Para disponibilidad específica de un día

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "is_recurring")
    private Boolean isRecurring = true; // true = regla recurrente, false = fecha específica

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    /**
     * Verificar si aplica para una fecha específica
     */
    @Transient
    public boolean appliesTo(LocalDate date) {
        if (!isRecurring && specificDate != null) {
            return specificDate.equals(date);
        }
        if (isRecurring && dayOfWeek != null) {
            return date.getDayOfWeek() == dayOfWeek;
        }
        return false;
    }
}
