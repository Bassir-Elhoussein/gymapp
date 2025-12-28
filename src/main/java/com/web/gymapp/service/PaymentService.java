package com.web.gymapp.service;

import com.web.gymapp.model.Payment;
import com.web.gymapp.model.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {

    /**
     * Process a new payment for a subscription
     * Automatically updates subscription payment totals
     */
    Payment processPayment(Long subscriptionId, Double amount, PaymentMethod method, String notes, Long processedByUserId);

    /**
     * Get payment by ID
     */
    Optional<Payment> getPaymentById(Long id);

    /**
     * Get all payments
     */
    List<Payment> getAllPayments();

    /**
     * Get all payments for a specific subscription
     */
    List<Payment> getPaymentsBySubscriptionId(Long subscriptionId);

    /**
     * Get all payments for a specific client (across all subscriptions)
     */
    List<Payment> getPaymentsByClientId(Long clientId);

    /**
     * Get payments by payment method
     */
    List<Payment> getPaymentsByMethod(PaymentMethod method);

    /**
     * Get payments within a date range
     */
    List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get payments processed by a specific user
     */
    List<Payment> getPaymentsByUserId(Long userId);

    /**
     * Get today's payments
     */
    public List<Payment> findTodaysPayments();

    /**
     * Get recent payments (last N records)
     */
    List<Payment> getRecentPayments(int limit);

    /**
     * Get payments by amount range
     */
    List<Payment> getPaymentsByAmountRange(Double minAmount, Double maxAmount);

    /**
     * Get latest payment for a subscription
     */
    Optional<Payment> getLatestPaymentForSubscription(Long subscriptionId);

    /**
     * Calculate total revenue within date range
     */
    Double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Calculate revenue by payment method
     */
    Double calculateRevenueByMethod(PaymentMethod method, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Calculate today's revenue
     */
    Double calculateTodaysRevenue();

    /**
     * Count total payments
     */
    Long countTotalPayments();

    /**
     * Count payments by method
     */
    Long countPaymentsByMethod(PaymentMethod method);
}