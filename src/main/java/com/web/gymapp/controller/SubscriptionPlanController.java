package com.web.gymapp.controller;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.SubscriptionPlan;
import com.web.gymapp.service.ClientService;
import com.web.gymapp.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService service;

    @PostMapping
    public SubscriptionPlan create(@RequestBody SubscriptionPlan plan) {
        return service.create(plan);
    }

    @GetMapping
    public List<SubscriptionPlan> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SubscriptionPlan getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public SubscriptionPlan update(@PathVariable Long id, @RequestBody SubscriptionPlan plan) {
        return service.update(id, plan);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
