package com.web.gymapp.service;

import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    /**
     * Create a new subscription for a client
     * Automatically calculates end date based on plan duration
     */
    Subscription createSubscription(Long clientId, Long planId, LocalDate startDate);

    /**
     * Update an existing subscription
     */
    Subscription updateSubscription(Long id, Subscription subscription);

    /**
     * Delete a subscription
     */
    void deleteSubscription(Long id);

    /**
     * Get subscription by ID
     */
    Optional<Subscription> getSubscriptionById(Long id);

    /**
     * Get all subscriptions
     */
    List<Subscription> getAllSubscriptions();

    /**
     * Get all subscriptions for a specific client (history)
     */
    List<Subscription> getSubscriptionsByClientId(Long clientId);

    /**
     * Get active subscription for a client (currently valid)
     */
    Optional<Subscription> getActiveSubscriptionByClientId(Long clientId);

    /**
     * Get subscriptions by status
     */
    List<Subscription> getSubscriptionsByStatus(SubscriptionStatus status);

    /**
     * Get subscriptions by client and status
     */
    List<Subscription> getSubscriptionsByClientIdAndStatus(Long clientId, SubscriptionStatus status);

    /**
     * Update subscription status
     */
    void updateSubscriptionStatus(Long subscriptionId, SubscriptionStatus status);

    /**
     * Automatically expire old subscriptions (scheduled job should call this)
     */
    void expireOldSubscriptions();

    /**
     * Get subscriptions expiring soon (within X days)
     */
    List<Subscription> getSubscriptionsExpiringSoon(int days);

    /**
     * Get subscriptions with remaining balance
     */
    List<Subscription> getSubscriptionsWithBalance();

    /**
     * Get fully paid subscriptions
     */
    List<Subscription> getFullyPaidSubscriptions();

    /**
     * Get subscriptions by plan
     */
    List<Subscription> getSubscriptionsByPlanId(Long planId);

    /**
     * Get active subscriptions by plan
     */
    List<Subscription> getActiveSubscriptionsByPlanId(Long planId);

    /**
     * Get recent subscriptions (last N records)
     */
    List<Subscription> getRecentSubscriptions(int limit);

    /**
     * Get subscriptions created within date range
     */
    List<Subscription> getSubscriptionsCreatedBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Get subscriptions starting within date range
     */
    List<Subscription> getSubscriptionsStartingBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Get subscriptions ending within date range
     */
    List<Subscription> getSubscriptionsEndingBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count active subscriptions
     */
    Long countActiveSubscriptions();

    /**
     * Count subscriptions by status
     */
    Long countByStatus(SubscriptionStatus status);

    /**
     * Calculate total revenue from all subscriptions
     */
    Double calculateTotalRevenue();

    /**
     * Calculate total outstanding balance
     */
    Double calculateTotalOutstandingBalance();

    /**
     * Renew a subscription (create new subscription after current one expires)
     */
    Subscription renewSubscription(Long currentSubscriptionId, Long newPlanId);
}