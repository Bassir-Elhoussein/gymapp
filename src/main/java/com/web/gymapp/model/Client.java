package com.web.gymapp.model;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */



import com.web.gymapp.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    // Fingerprint integration fields
    @Column(length = 2000)
    private String fingerprintData; // Store fingerprint template from device

    @Column(unique = true)
    private String fingerprintId; // Unique ID used by fingerprint machine

    // Keep history of all subscriptions
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    // Keep history of all attendance records
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Attendance> attendances;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Helper method to get current active subscription
    public Subscription getCurrentSubscription() {
        if (subscriptions == null || subscriptions.isEmpty()) {
            return null;
        }

        LocalDate today = LocalDate.now();
        return subscriptions.stream()
                .filter(sub -> sub.getStatus() == com.web.gymapp.model.enums.SubscriptionStatus.ACTIVE)
                .filter(sub -> !today.isBefore(sub.getStartDate()) && !today.isAfter(sub.getEndDate()))
                .findFirst()
                .orElse(null);
    }

    // Check if client can access gym right now
    public boolean canAccessGym() {
        Subscription current = getCurrentSubscription();
        return current != null && current.canAccessGym();
    }
}