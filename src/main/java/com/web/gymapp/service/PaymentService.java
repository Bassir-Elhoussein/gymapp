package com.web.gymapp.service;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */
import com.web.gymapp.model.Payment;

import java.util.List;

public interface PaymentService {
    Payment create(Payment payment);
    Payment getById(Long id);
    List<Payment> getAll();
}