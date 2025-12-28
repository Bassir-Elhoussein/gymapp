package com.web.gymapp.repository;

import com.web.gymapp.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    // Find plan by name
    Optional<SubscriptionPlan> findByName(String name);

    // Find all active plans
    List<SubscriptionPlan> findByIsActiveTrue();

    // Find all inactive plans
    List<SubscriptionPlan> findByIsActiveFalse();

    // Find plans by duration
    List<SubscriptionPlan> findByDurationMonths(Integer durationMonths);

    // Find plans by price range
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.price ASC")
    List<SubscriptionPlan> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Find active plans ordered by price
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true ORDER BY p.price ASC")
    List<SubscriptionPlan> findActivePlansOrderedByPrice();

    // Find active plans ordered by duration
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true ORDER BY p.durationMonths ASC")
    List<SubscriptionPlan> findActivePlansOrderedByDuration();

    // Check if plan name exists
    boolean existsByName(String name);

    // Check if plan name exists (excluding specific ID - for updates)
    @Query("SELECT COUNT(p) > 0 FROM SubscriptionPlan p WHERE p.name = :name AND p.id != :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    // Count active plans
    @Query("SELECT COUNT(p) FROM SubscriptionPlan p WHERE p.isActive = true")
    Long countActivePlans();

    // Count inactive plans
    @Query("SELECT COUNT(p) FROM SubscriptionPlan p WHERE p.isActive = false")
    Long countInactivePlans();

    // Get most popular plans (by subscription count)
    @Query("SELECT p FROM SubscriptionPlan p JOIN p.subscriptions s GROUP BY p ORDER BY COUNT(s) DESC")
    List<SubscriptionPlan> findMostPopularPlans();

    // Find plans with name containing (search)
    @Query("SELECT p FROM SubscriptionPlan p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SubscriptionPlan> searchByName(@Param("keyword") String keyword);
}