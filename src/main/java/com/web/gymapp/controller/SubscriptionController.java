package com.web.gymapp.controller;

import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.enums.SubscriptionStatus;
import com.web.gymapp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Create a new subscription
     * POST /api/subscriptions?clientId=1&planId=1&startDate=2024-01-01
     */
    @PostMapping
    public ResponseEntity<Subscription> createSubscription(
            @RequestParam Long clientId,
            @RequestParam Long planId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        Subscription created = subscriptionService.createSubscription(clientId, planId, startDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update a subscription
     * PUT /api/subscriptions/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(
            @PathVariable Long id,
            @RequestBody Subscription subscription) {
        Subscription updated = subscriptionService.updateSubscription(id, subscription);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a subscription
     * DELETE /api/subscriptions/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get subscription by ID
     * GET /api/subscriptions/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all subscriptions
     * GET /api/subscriptions
     */
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    /**
     * Get all subscriptions for a client (history)
     * GET /api/subscriptions/client/1
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Subscription>> getClientSubscriptions(@PathVariable Long clientId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByClientId(clientId));
    }

    /**
     * Get active subscription for a client
     * GET /api/subscriptions/client/1/active
     */
    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<Subscription> getActiveSubscription(@PathVariable Long clientId) {
        return subscriptionService.getActiveSubscriptionByClientId(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get subscriptions by status
     * GET /api/subscriptions/status/ACTIVE
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByStatus(@PathVariable SubscriptionStatus status) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByStatus(status));
    }

    /**
     * Get subscriptions by client and status
     * GET /api/subscriptions/client/1/status/ACTIVE
     */
    @GetMapping("/client/{clientId}/status/{status}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByClientAndStatus(
            @PathVariable Long clientId,
            @PathVariable SubscriptionStatus status) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByClientIdAndStatus(clientId, status));
    }

    /**
     * Update subscription status
     * PATCH /api/subscriptions/1/status?status=SUSPENDED
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam SubscriptionStatus status) {
        subscriptionService.updateSubscriptionStatus(id, status);
        return ResponseEntity.ok().build();
    }

    /**
     * Expire old subscriptions (run this as scheduled job)
     * POST /api/subscriptions/expire-old
     */
    @PostMapping("/expire-old")
    public ResponseEntity<Void> expireOldSubscriptions() {
        subscriptionService.expireOldSubscriptions();
        return ResponseEntity.ok().build();
    }

    /**
     * Get subscriptions expiring soon
     * GET /api/subscriptions/expiring-soon?days=7
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<Subscription>> getExpiringSoon(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsExpiringSoon(days));
    }

    /**
     * Get subscriptions with remaining balance
     * GET /api/subscriptions/with-balance
     */
    @GetMapping("/with-balance")
    public ResponseEntity<List<Subscription>> getWithBalance() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsWithBalance());
    }

    /**
     * Get fully paid subscriptions
     * GET /api/subscriptions/fully-paid
     */
    @GetMapping("/fully-paid")
    public ResponseEntity<List<Subscription>> getFullyPaid() {
        return ResponseEntity.ok(subscriptionService.getFullyPaidSubscriptions());
    }

    /**
     * Get subscriptions by plan
     * GET /api/subscriptions/plan/1
     */
    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByPlanId(planId));
    }

    /**
     * Get active subscriptions by plan
     * GET /api/subscriptions/plan/1/active
     */
    @GetMapping("/plan/{planId}/active")
    public ResponseEntity<List<Subscription>> getActiveSubscriptionsByPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptionsByPlanId(planId));
    }

    /**
     * Get recent subscriptions
     * GET /api/subscriptions/recent?limit=10
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Subscription>> getRecent(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(subscriptionService.getRecentSubscriptions(limit));
    }

    /**
     * Get subscriptions created within date range
     * GET /api/subscriptions/created-between?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/created-between")
    public ResponseEntity<List<Subscription>> getCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsCreatedBetween(startDate, endDate));
    }

    /**
     * Get subscriptions starting within date range
     * GET /api/subscriptions/starting-between?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/starting-between")
    public ResponseEntity<List<Subscription>> getStartingBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsStartingBetween(startDate, endDate));
    }

    /**
     * Get subscriptions ending within date range
     * GET /api/subscriptions/ending-between?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/ending-between")
    public ResponseEntity<List<Subscription>> getEndingBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsEndingBetween(startDate, endDate));
    }

    /**
     * Renew a subscription
     * POST /api/subscriptions/1/renew?newPlanId=2
     */
    @PostMapping("/{id}/renew")
    public ResponseEntity<Subscription> renewSubscription(
            @PathVariable Long id,
            @RequestParam Long newPlanId) {
        Subscription renewed = subscriptionService.renewSubscription(id, newPlanId);
        return ResponseEntity.status(HttpStatus.CREATED).body(renewed);
    }

    /**
     * Get subscription statistics
     * GET /api/subscriptions/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Long activeCount = subscriptionService.countActiveSubscriptions();
        Long expiredCount = subscriptionService.countByStatus(SubscriptionStatus.EXPIRED);
        Long suspendedCount = subscriptionService.countByStatus(SubscriptionStatus.SUSPENDED);
        Double totalRevenue = subscriptionService.calculateTotalRevenue();
        Double outstandingBalance = subscriptionService.calculateTotalOutstandingBalance();
        List<Subscription> expiringSoon = subscriptionService.getSubscriptionsExpiringSoon(7);
        List<Subscription> unpaid = subscriptionService.getSubscriptionsWithBalance();

        Map<String, Object> stats = Map.of(
                "activeSubscriptions", activeCount,
                "expiredSubscriptions", expiredCount,
                "suspendedSubscriptions", suspendedCount,
                "totalRevenue", totalRevenue,
                "outstandingBalance", outstandingBalance,
                "expiringSoonCount", expiringSoon.size(),
                "unpaidCount", unpaid.size()
        );

        return ResponseEntity.ok(stats);
    }
}