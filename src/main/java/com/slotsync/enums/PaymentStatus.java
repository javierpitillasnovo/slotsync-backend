package com.slotsync.enums;

/**
 * Estados de pago
 */
public enum PaymentStatus {
    PENDING,          // Pendiente
    PROCESSING,       // Procesando
    COMPLETED,        // Completado
    FAILED,           // Fallido
    REFUNDED,         // Reembolsado
    CANCELLED         // Cancelado
}
