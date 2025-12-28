package com.web.gymapp.repository;

import com.web.gymapp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Find client by phone number
    Optional<Client> findByPhone(String phone);

    // Find client by fingerprint ID
    Optional<Client> findByFingerprintId(String fingerprintId);

    // Find client by email
    Optional<Client> findByEmail(String email);

    // Search clients by name (case-insensitive, partial match)
    List<Client> findByFullNameContainingIgnoreCase(String name);

    // Check if phone number already exists
    boolean existsByPhone(String phone);

    // Check if fingerprint ID already exists
    boolean existsByFingerprintId(String fingerprintId);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find clients with active subscriptions
    @Query("SELECT DISTINCT c FROM Client c JOIN c.subscriptions s WHERE s.status = 'ACTIVE' AND s.startDate <= CURRENT_DATE AND s.endDate >= CURRENT_DATE")
    List<Client> findClientsWithActiveSubscriptions();

    // Find clients with expired subscriptions
    @Query("SELECT DISTINCT c FROM Client c JOIN c.subscriptions s WHERE s.status = 'EXPIRED' OR s.endDate < CURRENT_DATE")
    List<Client> findClientsWithExpiredSubscriptions();

    // Find clients without any subscription
    @Query("SELECT c FROM Client c WHERE c.subscriptions IS EMPTY")
    List<Client> findClientsWithoutSubscriptions();

    // Find clients with unpaid balances
    @Query("SELECT DISTINCT c FROM Client c JOIN c.subscriptions s WHERE s.remainingBalance > 0 AND s.status = 'ACTIVE'")
    List<Client> findClientsWithUnpaidBalances();

    // Count total clients
    @Query("SELECT COUNT(c) FROM Client c")
    Long countTotalClients();

    // Count clients with active subscriptions
    @Query("SELECT COUNT(DISTINCT c) FROM Client c JOIN c.subscriptions s WHERE s.status = 'ACTIVE' AND s.startDate <= CURRENT_DATE AND s.endDate >= CURRENT_DATE")
    Long countActiveClients();
}