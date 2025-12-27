package com.web.gymapp.service.impl;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */
import java.time.LocalDateTime;
import java.util.List;
import com.web.gymapp.model.enums.SubscriptionStatus;
import com.web.gymapp.model.Client;
import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.SubscriptionPlan;
import com.web.gymapp.repository.ClientRepository;
import com.web.gymapp.repository.SubscriptionPlanRepository;
import com.web.gymapp.repository.SubscriptionRepository;
import com.web.gymapp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repo;
    private final ClientRepository clientRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;


    public Subscription create(Subscription dto) {

        Client client = clientRepository.findById(dto.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        SubscriptionPlan plan = subscriptionPlanRepository.findById(dto.getPlan().getId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Subscription sub = new Subscription();

        sub.setClient(client);
        sub.setPlan(plan);
        sub.setStartDate(dto.getStartDate());
        sub.setEndDate(dto.getEndDate());
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setCreatedAt(LocalDateTime.now());

        return repo.save(sub);
    }
    @Override
    public Subscription update(Long id, Subscription subscription) {
        Subscription existing = getById(id);
        existing.setClient(subscription.getClient());
        existing.setPlan(subscription.getPlan());
        existing.setStartDate(subscription.getStartDate());
        existing.setEndDate(subscription.getEndDate());
        existing.setStatus(subscription.getStatus());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Subscription getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
    }

    @Override
    public List<Subscription> getAll() {
        return repo.findAll();
    }
}