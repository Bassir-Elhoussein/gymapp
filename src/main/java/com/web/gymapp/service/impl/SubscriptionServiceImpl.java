package com.web.gymapp.service.impl;

import com.web.gymapp.model.Client;
import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.SubscriptionPlan;
import com.web.gymapp.model.enums.SubscriptionStatus;
import com.web.gymapp.repository.ClientRepository;
import com.web.gymapp.repository.SubscriptionPlanRepository;
import com.web.gymapp.repository.SubscriptionRepository;
import com.web.gymapp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final SubscriptionPlanRepository planRepository;

    @Override
    @Transactional
    public Subscription createSubscription(Long clientId, Long planId, LocalDate startDate) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));

        // Calculate end date based on plan duration
        LocalDate endDate = plan.calculateEndDate(startDate);

        Subscription subscription = Subscription.builder()
                .client(client)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .totalPrice(plan.getPrice())
                .build();

        return subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public Subscription updateSubscription(Long id, Subscription subscription) {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));

        existing.setStartDate(subscription.getStartDate());
        existing.setEndDate(subscription.getEndDate());
        existing.setStatus(subscription.getStatus());
        existing.setTotalPrice(subscription.getTotalPrice());

        return subscriptionRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteSubscription(Long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new RuntimeException("Subscription not found with id: " + id);
        }
        subscriptionRepository.deleteById(id);
    }

    @Override
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> getSubscriptionsByClientId(Long clientId) {
        return subscriptionRepository.findByClientId(clientId);
    }

    @Override
    public Optional<Subscription> getActiveSubscriptionByClientId(Long clientId) {
        return subscriptionRepository.findActiveSubscriptionByClientId(clientId, LocalDate.now());
    }

    @Override
    public List<Subscription> getSubscriptionsByStatus(SubscriptionStatus status) {
        return subscriptionRepository.findByStatus(status);
    }

    @Override
    public List<Subscription> getSubscriptionsByClientIdAndStatus(Long clientId, SubscriptionStatus status) {
        return subscriptionRepository.findByClientIdAndStatus(clientId, status);
    }

    @Override
    @Transactional
    public void updateSubscriptionStatus(Long subscriptionId, SubscriptionStatus status) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
        subscription.setStatus(status);
        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void expireOldSubscriptions() {
        List<Subscription> expiredSubs = subscriptionRepository.findExpiredSubscriptions(LocalDate.now());
        for (Subscription sub : expiredSubs) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(sub);
        }
    }

    @Override
    public List<Subscription> getSubscriptionsExpiringSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);
        return subscriptionRepository.findSubscriptionsExpiringSoon(today, futureDate);
    }

    @Override
    public List<Subscription> getSubscriptionsWithBalance() {
        return subscriptionRepository.findSubscriptionsWithBalance();
    }

    @Override
    public List<Subscription> getFullyPaidSubscriptions() {
        return subscriptionRepository.findFullyPaidSubscriptions();
    }

    @Override
    public List<Subscription> getSubscriptionsByPlanId(Long planId) {
        return subscriptionRepository.findByPlanId(planId);
    }

    @Override
    public List<Subscription> getActiveSubscriptionsByPlanId(Long planId) {
        return subscriptionRepository.findActiveSubscriptionsByPlanId(planId);
    }

    @Override
    public List<Subscription> getRecentSubscriptions(int limit) {
        return subscriptionRepository.findRecentSubscriptions()
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<Subscription> getSubscriptionsCreatedBetween(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Subscription> getSubscriptionsStartingBetween(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.findByStartDateBetween(startDate, endDate);
    }

    @Override
    public List<Subscription> getSubscriptionsEndingBetween(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.findByEndDateBetween(startDate, endDate);
    }

    @Override
    public Long countActiveSubscriptions() {
        return subscriptionRepository.countActiveSubscriptions(LocalDate.now());
    }

    @Override
    public Long countByStatus(SubscriptionStatus status) {
        return subscriptionRepository.countByStatus(status);
    }

    @Override
    public Double calculateTotalRevenue() {
        Double revenue = subscriptionRepository.calculateTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public Double calculateTotalOutstandingBalance() {
        Double balance = subscriptionRepository.calculateTotalOutstandingBalance();
        return balance != null ? balance : 0.0;
    }

    @Override
    @Transactional
    public Subscription renewSubscription(Long currentSubscriptionId, Long newPlanId) {
        Subscription currentSub = subscriptionRepository.findById(currentSubscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + currentSubscriptionId));

        // Mark current subscription as expired
        currentSub.setStatus(SubscriptionStatus.EXPIRED);
        subscriptionRepository.save(currentSub);

        // Create new subscription starting after current one ends (or today if already expired)
        LocalDate newStartDate = currentSub.getEndDate().isAfter(LocalDate.now())
                ? currentSub.getEndDate().plusDays(1)
                : LocalDate.now();

        return createSubscription(currentSub.getClient().getId(), newPlanId, newStartDate);
    }
}