package com.web.gymapp.model;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Subscription subscription;

    private Double totalPrice;     // price for this subscription
    private Double amountGiven;    // money client gave
    private Double rest;           // auto-calculated but staff can override

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private LocalDateTime paymentDate;

    @PrePersist
    public void prePersist() {
        this.paymentDate = LocalDateTime.now();
        this.method = PaymentMethod.CASH;

        if (this.rest == null && totalPrice != null && amountGiven != null) {
            this.rest = amountGiven - totalPrice;
        }
    }

}

