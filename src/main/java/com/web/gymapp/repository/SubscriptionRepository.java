package com.web.gymapp.repository;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */
import com.web.gymapp.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("""
        SELECT s FROM Subscription s
        WHERE s.client.id = :clientId
        ORDER BY s.endDate DESC
    """)
    List<Subscription> findLatestByClient(@Param("clientId") Long clientId);
}