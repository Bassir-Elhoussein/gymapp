package com.web.gymapp.controller;

import com.web.gymapp.model.SubscriptionPlan;
import com.web.gymapp.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    /**
     * Create a new subscription plan
     * POST /api/plans
     */
    @PostMapping
    public ResponseEntity<SubscriptionPlan> createPlan(@RequestBody SubscriptionPlan plan) {
        SubscriptionPlan created = planService.createPlan(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing plan
     * PUT /api/plans/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> updatePlan(
            @PathVariable Long id,
            @RequestBody SubscriptionPlan plan) {
        SubscriptionPlan updated = planService.updatePlan(id, plan);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a plan
     * DELETE /api/plans/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get plan by ID
     * GET /api/plans/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> getPlanById(@PathVariable Long id) {
        return planService.getPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get plan by name
     * GET /api/plans/name/6 Months
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<SubscriptionPlan> getPlanByName(@PathVariable String name) {
        return planService.getPlanByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all plans
     * GET /api/plans
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionPlan>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    /**
     * Get all active plans
     * GET /api/plans/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionPlan>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }

    /**
     * Get all inactive plans
     * GET /api/plans/inactive
     */
    @GetMapping("/inactive")
    public ResponseEntity<List<SubscriptionPlan>> getInactivePlans() {
        return ResponseEntity.ok(planService.getInactivePlans());
    }

    /**
     * Get plans by duration
     * GET /api/plans/duration/6
     */
    @GetMapping("/duration/{months}")
    public ResponseEntity<List<SubscriptionPlan>> getPlansByDuration(@PathVariable Integer months) {
        return ResponseEntity.ok(planService.getPlansByDuration(months));
    }

    /**
     * Get plans within price range
     * GET /api/plans/price-range?minPrice=100&maxPrice=500
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<SubscriptionPlan>> getPlansByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return ResponseEntity.ok(planService.getPlansByPriceRange(minPrice, maxPrice));
    }

    /**
     * Get active plans ordered by price
     * GET /api/plans/active/by-price
     */
    @GetMapping("/active/by-price")
    public ResponseEntity<List<SubscriptionPlan>> getActivePlansByPrice() {
        return ResponseEntity.ok(planService.getActivePlansOrderedByPrice());
    }

    /**
     * Get active plans ordered by duration
     * GET /api/plans/active/by-duration
     */
    @GetMapping("/active/by-duration")
    public ResponseEntity<List<SubscriptionPlan>> getActivePlansByDuration() {
        return ResponseEntity.ok(planService.getActivePlansOrderedByDuration());
    }

    /**
     * Toggle plan status
     * PATCH /api/plans/1/toggle-status?isActive=true
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> togglePlanStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        planService.togglePlanStatus(id, isActive);
        return ResponseEntity.ok().build();
    }

    /**
     * Activate a plan
     * PATCH /api/plans/1/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activatePlan(@PathVariable Long id) {
        planService.activatePlan(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Deactivate a plan
     * PATCH /api/plans/1/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePlan(@PathVariable Long id) {
        planService.deactivatePlan(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Search plans by name
     * GET /api/plans/search?keyword=month
     */
    @GetMapping("/search")
    public ResponseEntity<List<SubscriptionPlan>> searchPlans(@RequestParam String keyword) {
        return ResponseEntity.ok(planService.searchPlansByName(keyword));
    }

    /**
     * Get most popular plans (by subscription count)
     * GET /api/plans/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<List<SubscriptionPlan>> getPopularPlans() {
        return ResponseEntity.ok(planService.getMostPopularPlans());
    }

    /**
     * Check if plan name is available
     * GET /api/plans/check-name?name=6 Months
     */
    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkPlanName(@RequestParam String name) {
        boolean available = planService.isPlanNameAvailable(name);
        return ResponseEntity.ok(Map.of("available", available));
    }

    /**
     * Check if plan name is available for update
     * GET /api/plans/check-name-update?name=6 Months&planId=1
     */
    @GetMapping("/check-name-update")
    public ResponseEntity<Map<String, Boolean>> checkPlanNameForUpdate(
            @RequestParam String name,
            @RequestParam Long planId) {
        boolean available = planService.isPlanNameAvailableForUpdate(name, planId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    /**
     * Get plan statistics
     * GET /api/plans/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPlanStats() {
        Long totalPlans = (long) planService.getAllPlans().size();
        Long activePlans = planService.countActivePlans();
        Long inactivePlans = planService.countInactivePlans();
        List<SubscriptionPlan> popularPlans = planService.getMostPopularPlans();

        // Get most expensive and cheapest plans
        List<SubscriptionPlan> allPlans = planService.getAllPlans();
        Double maxPrice = allPlans.stream()
                .map(SubscriptionPlan::getPrice)
                .max(Double::compareTo)
                .orElse(0.0);
        Double minPrice = allPlans.stream()
                .map(SubscriptionPlan::getPrice)
                .min(Double::compareTo)
                .orElse(0.0);

        Map<String, Object> stats = Map.of(
                "totalPlans", totalPlans,
                "activePlans", activePlans,
                "inactivePlans", inactivePlans,
                "maxPrice", maxPrice,
                "minPrice", minPrice,
                "popularPlanCount", popularPlans.size()
        );

        return ResponseEntity.ok(stats);
    }

    /**
     * Get plan details with calculations
     * GET /api/plans/1/details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getPlanDetails(@PathVariable Long id) {

        SubscriptionPlan plan = planService.getPlanById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Map<String, Object> details = Map.ofEntries(
                Map.entry("id", plan.getId()),
                Map.entry("name", plan.getName()),
                Map.entry("price", plan.getPrice()),
                Map.entry("durationMonths", plan.getDurationMonths()),
                Map.entry("pricePerMonth", plan.getPricePerMonth()),
                Map.entry("displayName", plan.getDisplayName()),
                Map.entry("description", plan.getDescription() != null ? plan.getDescription() : ""),
                Map.entry("isActive", plan.getIsActive()),
                Map.entry("isAvailable", plan.isAvailable()),
                Map.entry("createdAt", plan.getCreatedAt()),
                Map.entry("updatedAt", plan.getUpdatedAt())
        );

        return ResponseEntity.ok(details);
    }

}