package com.web.gymapp.service;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.Client;

import java.util.List;

public interface ClientService {
    Client create(Client client);
    Client update(Long id, Client client);
    void delete(Long id);
    Client getById(Long id);
    List<Client> getAll();
}
