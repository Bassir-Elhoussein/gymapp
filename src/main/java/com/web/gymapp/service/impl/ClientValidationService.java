package com.web.gymapp.service.impl;

import com.web.gymapp.model.Subscription;
import com.web.gymapp.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Author: Bassir El Houssein
 * Date: 11/29/2025
 */
@Service
@RequiredArgsConstructor
public class ClientValidationService {

    private final SubscriptionRepository subscriptionRepository;

    public boolean isClientValid(Long clientId) {
        List<Subscription> subs = subscriptionRepository.findLatestByClient(clientId);

        if (subs.isEmpty()) {
            return false; // no subscription, not valid
        }

        Subscription last = subs.get(0);
        LocalDate today = LocalDate.now();

        boolean isActive =
                last.getStatus().name().equalsIgnoreCase("ACTIVE") &&
                        (today.isEqual(last.getStartDate()) || today.isAfter(last.getStartDate())) &&
                        (today.isEqual(last.getEndDate()) || today.isBefore(last.getEndDate()));


        return isActive;
    }
}

