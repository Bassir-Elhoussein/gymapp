package com.web.gymapp.repository;

import com.web.gymapp.model.Payment;
import com.web.gymapp.model.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find all payments for a specific subscription
    List<Payment> findBySubscriptionId(Long subscriptionId);

    // Find payments by payment method
    List<Payment> findByMethod(PaymentMethod method);

    // Find payments within a date range
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find all payments for a specific client (through subscription)
    @Query("SELECT p FROM Payment p WHERE p.subscription.client.id = :clientId ORDER BY p.paymentDate DESC")
    List<Payment> findByClientId(@Param("clientId") Long clientId);

    // Find payments processed by a specific user
    @Query("SELECT p FROM Payment p WHERE p.processedBy.id = :userId ORDER BY p.paymentDate DESC")
    List<Payment> findByProcessedByUserId(@Param("userId") Long userId);

    // Calculate total revenue within date range
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Double calculateTotalRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Calculate revenue by payment method within date range
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.method = :method AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double calculateRevenueByMethod(@Param("method") PaymentMethod method, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Count total payments
    @Query("SELECT COUNT(p) FROM Payment p")
    Long countTotalPayments();

    // Count payments by method
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.method = :method")
    Long countPaymentsByMethod(@Param("method") PaymentMethod method);

    // Get recent payments (last N payments)
    @Query("SELECT p FROM Payment p ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPayments();

    // Get today's payments
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startOfDay AND :endOfDay ORDER BY p.paymentDate DESC")
    List<Payment> findTodaysPayments(@Param("startOfDay") LocalDateTime startOfDay,
                                     @Param("endOfDay") LocalDateTime endOfDay);


    // Calculate today's revenue
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startOfDay AND :endOfDay")
    Double calculateTodaysRevenue(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);


    // Find payments by amount range
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount ORDER BY p.paymentDate DESC")
    List<Payment> findByAmountRange(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);

    // Get latest payment for a subscription
    @Query("SELECT p FROM Payment p WHERE p.subscription.id = :subscriptionId ORDER BY p.paymentDate DESC")
    List<Payment> findLatestPaymentBySubscriptionId(@Param("subscriptionId") Long subscriptionId);
}