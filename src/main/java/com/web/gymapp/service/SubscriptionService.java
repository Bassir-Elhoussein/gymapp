package com.web.gymapp.service;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */

import com.web.gymapp.model.Subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription create(Subscription subscription);
    Subscription update(Long id, Subscription subscription);
    void delete(Long id);
    Subscription getById(Long id);
    List<Subscription> getAll();
}