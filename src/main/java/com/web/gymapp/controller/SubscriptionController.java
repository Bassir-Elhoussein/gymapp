package com.web.gymapp.controller;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.Subscription;
import com.web.gymapp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;



    @PostMapping
    public Subscription create(@RequestBody Subscription subscription) {
        return service.create(subscription);
    }

    @GetMapping
    public List<Subscription> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Subscription getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Subscription update(@PathVariable Long id, @RequestBody Subscription subscription) {
        return service.update(id, subscription);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
