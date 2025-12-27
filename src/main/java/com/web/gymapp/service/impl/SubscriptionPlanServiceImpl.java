package com.web.gymapp.service.impl;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */
import com.web.gymapp.model.*;
import com.web.gymapp.repository.SubscriptionPlanRepository;
import com.web.gymapp.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository repository;

    @Override
    public SubscriptionPlan create(SubscriptionPlan plan) {
        return repository.save(plan);
    }

    @Override
    public SubscriptionPlan update(Long id, SubscriptionPlan plan) {
        SubscriptionPlan existing = getById(id);
        existing.setName(plan.getName());
        double price = plan.getPrice();
        existing.setPrice(price);
        existing.setDescription(plan.getDescription());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public SubscriptionPlan getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    @Override
    public List<SubscriptionPlan> getAll() {
        return repository.findAll();
    }
}