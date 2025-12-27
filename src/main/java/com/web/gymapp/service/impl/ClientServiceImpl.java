package com.web.gymapp.service.impl;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */

import com.web.gymapp.model.Client;
import com.web.gymapp.repository.ClientRepository;
import com.web.gymapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = getById(id);
        existing.setFullName(client.getFullName());
        existing.setPhone(client.getPhone());
        existing.setEmail(client.getEmail());
        existing.setGender(client.getGender());
        existing.setBirthDate(client.getBirthDate());
        return clientRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }
}