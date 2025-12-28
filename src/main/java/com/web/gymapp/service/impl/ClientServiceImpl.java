package com.web.gymapp.service.impl;

import com.web.gymapp.model.Client;
import com.web.gymapp.repository.ClientRepository;
import com.web.gymapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public Client createClient(Client client) {
        // Validate phone number uniqueness
        if (clientRepository.existsByPhone(client.getPhone())) {
            throw new RuntimeException("Client with phone number " + client.getPhone() + " already exists");
        }

        // Validate email uniqueness if provided
        if (client.getEmail() != null && !client.getEmail().isEmpty()) {
            if (clientRepository.existsByEmail(client.getEmail())) {
                throw new RuntimeException("Client with email " + client.getEmail() + " already exists");
            }
        }

        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Client updateClient(Long id, Client client) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        // Update fields
        existingClient.setFullName(client.getFullName());
        existingClient.setEmail(client.getEmail());
        existingClient.setGender(client.getGender());
        existingClient.setBirthDate(client.getBirthDate());

        // Only update phone if changed and not duplicate
        if (!existingClient.getPhone().equals(client.getPhone())) {
            if (clientRepository.existsByPhone(client.getPhone())) {
                throw new RuntimeException("Phone number " + client.getPhone() + " is already in use");
            }
            existingClient.setPhone(client.getPhone());
        }

        return clientRepository.save(existingClient);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Optional<Client> getClientByFingerprintId(String fingerprintId) {
        return clientRepository.findByFingerprintId(fingerprintId);
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        return clientRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public boolean canClientAccessGym(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        return client.canAccessGym();
    }

    @Override
    public List<Client> getClientsWithActiveSubscriptions() {
        return clientRepository.findClientsWithActiveSubscriptions();
    }

    @Override
    public List<Client> getClientsWithExpiredSubscriptions() {
        return clientRepository.findClientsWithExpiredSubscriptions();
    }

    @Override
    public List<Client> getClientsWithoutSubscriptions() {
        return clientRepository.findClientsWithoutSubscriptions();
    }

    @Override
    public List<Client> getClientsWithUnpaidBalances() {
        return clientRepository.findClientsWithUnpaidBalances();
    }

    @Override
    @Transactional
    public void registerFingerprint(Long clientId, String fingerprintData, String fingerprintId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        // Check if fingerprint ID is already registered to another client
        if (clientRepository.existsByFingerprintId(fingerprintId)) {
            Optional<Client> existingClient = clientRepository.findByFingerprintId(fingerprintId);
            if (existingClient.isPresent() && !existingClient.get().getId().equals(clientId)) {
                throw new RuntimeException("Fingerprint ID is already registered to another client");
            }
        }

        client.setFingerprintData(fingerprintData);
        client.setFingerprintId(fingerprintId);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public void removeFingerprint(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        client.setFingerprintData(null);
        client.setFingerprintId(null);
        clientRepository.save(client);
    }

    @Override
    public Long countTotalClients() {
        return clientRepository.countTotalClients();
    }

    @Override
    public Long countActiveClients() {
        return clientRepository.countActiveClients();
    }
}