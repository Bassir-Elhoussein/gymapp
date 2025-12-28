package com.web.gymapp.service;

import com.web.gymapp.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {

    /**
     * Create a new subscription plan
     */
    SubscriptionPlan createPlan(SubscriptionPlan plan);

    /**
     * Update an existing subscription plan
     */
    SubscriptionPlan updatePlan(Long id, SubscriptionPlan plan);

    /**
     * Delete a subscription plan
     */
    void deletePlan(Long id);

    /**
     * Get subscription plan by ID
     */
    Optional<SubscriptionPlan> getPlanById(Long id);

    /**
     * Get subscription plan by name
     */
    Optional<SubscriptionPlan> getPlanByName(String name);

    /**
     * Get all subscription plans
     */
    List<SubscriptionPlan> getAllPlans();

    /**
     * Get all active plans
     */
    List<SubscriptionPlan> getActivePlans();

    /**
     * Get all inactive plans
     */
    List<SubscriptionPlan> getInactivePlans();

    /**
     * Get plans by duration (in months)
     */
    List<SubscriptionPlan> getPlansByDuration(Integer durationMonths);

    /**
     * Get plans within a price range
     */
    List<SubscriptionPlan> getPlansByPriceRange(Double minPrice, Double maxPrice);

    /**
     * Get active plans ordered by price
     */
    List<SubscriptionPlan> getActivePlansOrderedByPrice();

    /**
     * Get active plans ordered by duration
     */
    List<SubscriptionPlan> getActivePlansOrderedByDuration();

    /**
     * Toggle plan status (activate/deactivate)
     */
    void togglePlanStatus(Long id, boolean isActive);

    /**
     * Activate a plan
     */
    void activatePlan(Long id);

    /**
     * Deactivate a plan
     */
    void deactivatePlan(Long id);

    /**
     * Search plans by name
     */
    List<SubscriptionPlan> searchPlansByName(String keyword);

    /**
     * Get most popular plans (by subscription count)
     */
    List<SubscriptionPlan> getMostPopularPlans();

    /**
     * Count active plans
     */
    Long countActivePlans();

    /**
     * Count inactive plans
     */
    Long countInactivePlans();

    /**
     * Check if plan name is available
     */
    boolean isPlanNameAvailable(String name);

    /**
     * Check if plan name is available for update (excluding current plan)
     */
    boolean isPlanNameAvailableForUpdate(String name, Long planId);
}