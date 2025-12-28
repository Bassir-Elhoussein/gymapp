package com.web.gymapp.repository;

import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Find all subscriptions for a specific client
    List<Subscription> findByClientId(Long clientId);

    // Find subscriptions by status
    List<Subscription> findByStatus(SubscriptionStatus status);

    // Find subscriptions by client and status
    @Query("SELECT s FROM Subscription s WHERE s.client.id = :clientId AND s.status = :status ORDER BY s.createdAt DESC")
    List<Subscription> findByClientIdAndStatus(@Param("clientId") Long clientId, @Param("status") SubscriptionStatus status);

    // Find active subscription for a client (current date within subscription period)
    @Query("SELECT s FROM Subscription s WHERE s.client.id = :clientId AND s.status = 'ACTIVE' AND s.startDate <= :today AND s.endDate >= :today")
    Optional<Subscription> findActiveSubscriptionByClientId(@Param("clientId") Long clientId, @Param("today") LocalDate today);

    // Find subscriptions that have expired (end date passed but status still ACTIVE)
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND s.endDate < :today")
    List<Subscription> findExpiredSubscriptions(@Param("today") LocalDate today);

    // Find subscriptions expiring soon (within X days)
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND s.endDate BETWEEN :today AND :futureDate ORDER BY s.endDate ASC")
    List<Subscription> findSubscriptionsExpiringSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);

    // Find subscriptions with remaining balance (unpaid or partially paid)
    @Query("SELECT s FROM Subscription s WHERE s.remainingBalance > 0 ORDER BY s.remainingBalance DESC")
    List<Subscription> findSubscriptionsWithBalance();

    // Find fully paid subscriptions
    @Query("SELECT s FROM Subscription s WHERE s.remainingBalance <= 0")
    List<Subscription> findFullyPaidSubscriptions();

    // Find subscriptions by plan
    List<Subscription> findByPlanId(Long planId);

    // Find active subscriptions by plan
    @Query("SELECT s FROM Subscription s WHERE s.plan.id = :planId AND s.status = 'ACTIVE'")
    List<Subscription> findActiveSubscriptionsByPlanId(@Param("planId") Long planId);

    // Count active subscriptions
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.status = 'ACTIVE' AND s.startDate <= :today AND s.endDate >= :today")
    Long countActiveSubscriptions(@Param("today") LocalDate today);

    // Count subscriptions by status
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.status = :status")
    Long countByStatus(@Param("status") SubscriptionStatus status);

    // Calculate total revenue from all subscriptions
    @Query("SELECT SUM(s.amountPaid) FROM Subscription s")
    Double calculateTotalRevenue();

    // Calculate total outstanding balance
    @Query("SELECT SUM(s.remainingBalance) FROM Subscription s WHERE s.remainingBalance > 0")
    Double calculateTotalOutstandingBalance();

    // Find subscriptions created within date range
    @Query("SELECT s FROM Subscription s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
    List<Subscription> findByCreatedAtBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find subscriptions that start within date range
    @Query("SELECT s FROM Subscription s WHERE s.startDate BETWEEN :startDate AND :endDate ORDER BY s.startDate ASC")
    List<Subscription> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find subscriptions that end within date range
    @Query("SELECT s FROM Subscription s WHERE s.endDate BETWEEN :startDate AND :endDate ORDER BY s.endDate ASC")
    List<Subscription> findByEndDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find recent subscriptions
    @Query("SELECT s FROM Subscription s ORDER BY s.createdAt DESC")
    List<Subscription> findRecentSubscriptions();
}