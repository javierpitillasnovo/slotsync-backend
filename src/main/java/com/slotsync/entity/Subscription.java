package com.slotsync.entity;

import com.slotsync.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa la suscripción de un negocio
 */
@Entity
@Table(name = "subscriptions", indexes = {
    @Index(name = "idx_subscription_business", columnList = "business_id"),
    @Index(name = "idx_subscription_stripe", columnList = "stripe_subscription_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionPlan plan;

    @Column(name = "stripe_subscription_id", unique = true)
    private String stripeSubscriptionId;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "stripe_price_id")
    private String stripePriceId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency = "EUR";

    @Column(name = "billing_cycle", length = 20)
    private String billingCycle = "monthly"; // monthly, yearly

    @Column(name = "status", length = 20)
    private String status; // active, canceled, past_due, trialing

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @Column(name = "current_period_start")
    private LocalDateTime currentPeriodStart;

    @Column(name = "current_period_end")
    private LocalDateTime currentPeriodEnd;

    @Column(name = "cancel_at_period_end")
    private Boolean cancelAtPeriodEnd = false;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Transient
    public boolean isActive() {
        return "active".equals(status) || "trialing".equals(status);
    }

    @Transient
    public boolean isInTrial() {
        return "trialing".equals(status) &&
               trialEndsAt != null &&
               trialEndsAt.isAfter(LocalDateTime.now());
    }
}
