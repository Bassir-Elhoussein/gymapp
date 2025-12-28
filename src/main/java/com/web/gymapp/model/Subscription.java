package com.web.gymapp.model;

import com.web.gymapp.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private Double amountPaid;

    @Column(nullable = false)
    private Double remainingBalance;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = SubscriptionStatus.ACTIVE;
        this.amountPaid = 0.0;

        if (this.totalPrice != null) {
            this.remainingBalance = this.totalPrice;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isFullyPaid() {
        return remainingBalance != null && remainingBalance <= 0;
    }

    public boolean canAccessGym() {
        LocalDate today = LocalDate.now();
        return this.status == SubscriptionStatus.ACTIVE
                && !today.isBefore(this.startDate)
                && !today.isAfter(this.endDate)
                && this.amountPaid > 0;
    }

    public Double getPaymentPercentage() {
        if (totalPrice == null || totalPrice == 0) return 0.0;
        return (amountPaid / totalPrice) * 100;
    }

    public boolean isDateValid() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(this.startDate) && !today.isAfter(this.endDate);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.endDate);
    }

    public void addPayment(Payment payment) {
        if (payments == null) {
            payments = new ArrayList<>();
        }
        payments.add(payment);
        payment.setSubscription(this);

        if (this.amountPaid == null) {
            this.amountPaid = 0.0;
        }
        this.amountPaid += payment.getAmount();
        this.remainingBalance = this.totalPrice - this.amountPaid;
    }

    public Long getDaysRemaining() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(endDate)) {
            return 0L;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(today, endDate);
    }

    public Long getDaysElapsed() {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            return 0L;
        }
        LocalDate effectiveDate = today.isAfter(endDate) ? endDate : today;
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, effectiveDate);
    }
}