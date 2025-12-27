package com.web.gymapp.service;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */
import com.web.gymapp.model.SubscriptionPlan;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlan create(SubscriptionPlan plan);
    SubscriptionPlan update(Long id, SubscriptionPlan plan);
    void delete(Long id);
    SubscriptionPlan getById(Long id);
    List<SubscriptionPlan> getAll();
}