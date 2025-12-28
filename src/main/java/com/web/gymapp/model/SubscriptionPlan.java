package com.web.gymapp.model;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(length = 1000)
    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "plan")
    private List<Subscription> subscriptions;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDate calculateEndDate(LocalDate startDate) {
        if (durationMonths == null || startDate == null) {
            return null;
        }
        return startDate.plusMonths(durationMonths).minusDays(1);
    }

    public Double getPricePerMonth() {
        if (price == null || durationMonths == null || durationMonths == 0) {
            return 0.0;
        }
        return price / durationMonths;
    }

    public String getDisplayName() {
        return String.format("%s - %.2f MAD", name, price);
    }

    public boolean isAvailable() {
        return isActive != null && isActive;
    }
}