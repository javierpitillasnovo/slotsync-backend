package com.slotsync.entity;

import com.slotsync.enums.SubscriptionPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un negocio en el sistema (tenant)
 */
@Entity
@Table(name = "businesses", indexes = {
    @Index(name = "idx_business_slug", columnList = "slug"),
    @Index(name = "idx_business_owner", columnList = "owner_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business extends BaseEntity {

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank(message = "El slug es obligatorio")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String slug; // Para URL pública: slotsync.com/book/{slug}

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 100)
    @Column(name = "industry", length = 100)
    private String industry; // Tipo de negocio: peluquería, clínica, etc.

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 255)
    @Column(length = 255)
    private String email;

    @Column(length = 500)
    private String website;

    // Configuración de plan de suscripción
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", nullable = false, length = 20)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    // Configuración del negocio
    @Column(name = "timezone", length = 50)
    private String timezone = "Europe/Madrid";

    @Column(name = "currency", length = 3)
    private String currency = "EUR";

    @Column(name = "locale", length = 10)
    private String locale = "es";

    @Column(name = "primary_color", length = 7)
    private String primaryColor = "#6366f1";

    // Configuración de reservas
    @Column(name = "max_advance_booking_days")
    private Integer maxAdvanceBookingDays = 90;

    @Column(name = "min_advance_booking_hours")
    private Integer minAdvanceBookingHours = 2;

    @Column(name = "default_slot_duration")
    private Integer defaultSlotDuration = 30;

    @Column(name = "cancellation_hours")
    private Integer cancellationHours = 24;

    @Column(name = "allow_customer_cancellation")
    private Boolean allowCustomerCancellation = true;

    @Column(name = "require_payment_upfront")
    private Boolean requirePaymentUpfront = false;

    @Column(name = "deposit_percentage")
    private Integer depositPercentage = 0;

    // Configuración de notificaciones
    @Column(name = "send_email_notifications")
    private Boolean sendEmailNotifications = true;

    @Column(name = "send_sms_notifications")
    private Boolean sendSmsNotifications = false;

    @Column(name = "reminder_hours_before")
    private Integer reminderHoursBefore = 24;

    // Widget configuration
    @Column(name = "widget_enabled")
    private Boolean widgetEnabled = true;

    @Column(name = "public_booking_enabled")
    private Boolean publicBookingEnabled = true;

    // Relación con el propietario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Relación con suscripción activa
    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL)
    private Subscription activeSubscription;

    // Relaciones one-to-many
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Location> locations = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Professional> professionals = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Obtener URL pública de reservas
     */
    @Transient
    public String getPublicBookingUrl() {
        return "https://slotsync.com/book/" + slug;
    }

    /**
     * Verificar si el plan permite una funcionalidad
     */
    @Transient
    public boolean isPlanAllowed(SubscriptionPlan requiredPlan) {
        int currentLevel = subscriptionPlan.ordinal();
        int requiredLevel = requiredPlan.ordinal();
        return currentLevel >= requiredLevel;
    }

    /**
     * Verificar si el plan es de pago (no trial)
     */
    @Transient
    public boolean isPaidPlan() {
        return subscriptionPlan != null;
    }
}
