package com.web.gymapp.model;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a single payment transaction
 * Client can make multiple payments for one subscription (partial payments)
 * Example: 6-month subscription, pay 50% now, 50% later
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Subscription subscription;

    @Column(nullable = false)
    private Double amount;



    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String notes; // Optional notes about this payment

    @ManyToOne
    private AppUser processedBy; // Staff member who processed this payment

    @Column(updatable = false)
    private LocalDateTime paymentDate; // Amount paid in THIS specific payment

    @PrePersist
    public void prePersist() {
        this.paymentDate = LocalDateTime.now();
        if (this.method == null) {
            this.method = PaymentMethod.CASH;
        }
    }

    // Helper to check if this is an initial payment
    public boolean isInitialPayment() {
        if (subscription == null || subscription.getPayments() == null || subscription.getPayments().isEmpty()) {
            return true;
        }
        return subscription.getPayments().get(0).equals(this);
    }

    // Helper to get remaining balance after this payment
    public Double getRemainingBalanceAfterPayment() {
        if (subscription == null) return null;
        return subscription.getTotalPrice() - subscription.getAmountPaid();
    }

    // Get payment percentage of total subscription price
    public Double getPaymentPercentage() {
        if (subscription == null || subscription.getTotalPrice() == 0) return 0.0;
        return (amount / subscription.getTotalPrice()) * 100;
    }
}

