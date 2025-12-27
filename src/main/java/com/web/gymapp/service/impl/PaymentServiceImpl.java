package com.web.gymapp.service.impl;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.Payment;
import com.web.gymapp.model.Subscription;
import com.web.gymapp.repository.PaymentRepository;
import com.web.gymapp.repository.SubscriptionRepository;
import com.web.gymapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repo;
    private final SubscriptionRepository repos;
    @Override
    public Payment create(Payment payment)   {

        if (payment.getRest() == null) {
            payment.setRest(payment.getAmountGiven() - payment.getTotalPrice());
        }

       
        Subscription sub = repos.findById(payment.getSubscription().getId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        payment.setSubscription(sub);
        return repo.save(payment);
    }

    @Override
    public Payment getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<Payment> getAll() {
        return repo.findAll();
    }
}
