package com.slotsync.enums;

/**
 * Estados de una reserva
 */
public enum BookingStatus {
    PENDING,          // Pendiente de confirmación
    CONFIRMED,        // Confirmada
    IN_PROGRESS,      // En progreso
    COMPLETED,        // Completada
    CANCELLED,        // Cancelada por el cliente
    NO_SHOW,          // El cliente no apareció
    RESCHEDULED       // Reprogramada
}
