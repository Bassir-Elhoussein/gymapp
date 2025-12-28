package com.web.gymapp.model;

import com.web.gymapp.model.enums.AccessResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Tracks every gym entry attempt (successful or denied)
 * Keeps complete history of all check-ins
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attendances")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Client client;

    @ManyToOne
    private Subscription subscription; // Which subscription was active during this check-in

    private LocalDate date;
    private LocalDateTime checkInTime;

    @Enumerated(EnumType.STRING)
    private AccessResult accessResult; // GRANTED, DENIED_EXPIRED, DENIED_UNPAID, etc.

    private String denialReason; // Detailed reason if access was denied

    private String fingerprintId; // ID from fingerprint device for this check-in

    @PrePersist
    public void prePersist() {
        this.date = LocalDate.now();
        this.checkInTime = LocalDateTime.now();

        if (this.accessResult == null) {
            this.accessResult = AccessResult.GRANTED;
        }
    }

    // Helper method to check if access was successful
    public boolean wasAccessGranted() {
        return this.accessResult == AccessResult.GRANTED;
    }
}