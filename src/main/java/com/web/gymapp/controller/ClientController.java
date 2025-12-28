package com.web.gymapp.controller;

import com.web.gymapp.model.Client;
import com.web.gymapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    /**
     * Create a new client
     * POST /api/clients
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client created = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing client
     * PUT /api/clients/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client updated = clientService.updateClient(id, client);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a client
     * DELETE /api/clients/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get client by ID
     * GET /api/clients/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all clients
     * GET /api/clients
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    /**
     * Get client by phone number
     * GET /api/clients/phone/0612345678
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Client> getClientByPhone(@PathVariable String phone) {
        return clientService.getClientByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get client by email
     * GET /api/clients/email/client@example.com
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        return clientService.getClientByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get client by fingerprint ID
     * GET /api/clients/fingerprint/FP123
     */
    @GetMapping("/fingerprint/{fingerprintId}")
    public ResponseEntity<Client> getClientByFingerprint(@PathVariable String fingerprintId) {
        return clientService.getClientByFingerprintId(fingerprintId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search clients by name
     * GET /api/clients/search?name=Ahmed
     */
    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String name) {
        return ResponseEntity.ok(clientService.searchClientsByName(name));
    }

    /**
     * Check if client can access gym
     * GET /api/clients/1/can-access
     */
    @GetMapping("/{id}/can-access")
    public ResponseEntity<Map<String, Boolean>> canAccessGym(@PathVariable Long id) {
        boolean canAccess = clientService.canClientAccessGym(id);
        return ResponseEntity.ok(Map.of("canAccess", canAccess));
    }

    /**
     * Get clients with active subscriptions
     * GET /api/clients/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<Client>> getActiveClients() {
        return ResponseEntity.ok(clientService.getClientsWithActiveSubscriptions());
    }

    /**
     * Get clients with expired subscriptions
     * GET /api/clients/expired
     */
    @GetMapping("/expired")
    public ResponseEntity<List<Client>> getExpiredClients() {
        return ResponseEntity.ok(clientService.getClientsWithExpiredSubscriptions());
    }

    /**
     * Get clients without any subscription
     * GET /api/clients/no-subscription
     */
    @GetMapping("/no-subscription")
    public ResponseEntity<List<Client>> getClientsWithoutSubscriptions() {
        return ResponseEntity.ok(clientService.getClientsWithoutSubscriptions());
    }

    /**
     * Get clients with unpaid balances
     * GET /api/clients/unpaid
     */
    @GetMapping("/unpaid")
    public ResponseEntity<List<Client>> getClientsWithUnpaidBalances() {
        return ResponseEntity.ok(clientService.getClientsWithUnpaidBalances());
    }

    /**
     * Register fingerprint for a client
     * POST /api/clients/1/fingerprint
     */
    @PostMapping("/{id}/fingerprint")
    public ResponseEntity<Void> registerFingerprint(
            @PathVariable Long id,
            @RequestBody Map<String, String> fingerprintData) {
        String data = fingerprintData.get("fingerprintData");
        String fingerprintId = fingerprintData.get("fingerprintId");
        clientService.registerFingerprint(id, data, fingerprintId);
        return ResponseEntity.ok().build();
    }

    /**
     * Remove fingerprint from a client
     * DELETE /api/clients/1/fingerprint
     */
    @DeleteMapping("/{id}/fingerprint")
    public ResponseEntity<Void> removeFingerprint(@PathVariable Long id) {
        clientService.removeFingerprint(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get client statistics
     * GET /api/clients/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getClientStats() {
        Long totalClients = clientService.countTotalClients();
        Long activeClients = clientService.countActiveClients();
        List<Client> expiredClients = clientService.getClientsWithExpiredSubscriptions();
        List<Client> noSubscription = clientService.getClientsWithoutSubscriptions();
        List<Client> unpaid = clientService.getClientsWithUnpaidBalances();

        Map<String, Object> stats = Map.of(
                "totalClients", totalClients,
                "activeClients", activeClients,
                "expiredClients", expiredClients.size(),
                "noSubscription", noSubscription.size(),
                "unpaidBalances", unpaid.size()
        );

        return ResponseEntity.ok(stats);
    }
}