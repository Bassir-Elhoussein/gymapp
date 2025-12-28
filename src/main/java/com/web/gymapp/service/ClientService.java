package com.web.gymapp.service;

import com.web.gymapp.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    /**
     * Create a new client
     */
    Client createClient(Client client);

    /**
     * Update an existing client
     */
    Client updateClient(Long id, Client client);

    /**
     * Delete a client by ID
     */
    void deleteClient(Long id);

    /**
     * Get client by ID
     */
    Optional<Client> getClientById(Long id);

    /**
     * Get all clients
     */
    List<Client> getAllClients();

    /**
     * Get client by phone number
     */
    Optional<Client> getClientByPhone(String phone);

    /**
     * Get client by email
     */
    Optional<Client> getClientByEmail(String email);

    /**
     * Get client by fingerprint ID
     */
    Optional<Client> getClientByFingerprintId(String fingerprintId);

    /**
     * Search clients by name (partial match)
     */
    List<Client> searchClientsByName(String name);

    /**
     * Check if client can access gym (has valid subscription)
     */
    boolean canClientAccessGym(Long clientId);

    /**
     * Get clients with active subscriptions
     */
    List<Client> getClientsWithActiveSubscriptions();

    /**
     * Get clients with expired subscriptions
     */
    List<Client> getClientsWithExpiredSubscriptions();

    /**
     * Get clients without any subscription
     */
    List<Client> getClientsWithoutSubscriptions();

    /**
     * Get clients with unpaid balances
     */
    List<Client> getClientsWithUnpaidBalances();

    /**
     * Register fingerprint data for a client
     */
    void registerFingerprint(Long clientId, String fingerprintData, String fingerprintId);

    /**
     * Remove fingerprint data for a client
     */
    void removeFingerprint(Long clientId);

    /**
     * Count total clients
     */
    Long countTotalClients();

    /**
     * Count clients with active subscriptions
     */
    Long countActiveClients();
}