package com.xavier.client_backend.services;

import com.xavier.client_backend.domain.entities.ClientEntity;

import java.util.List;

public interface ClientService {
    ClientEntity save(ClientEntity clientEntity);
    List<ClientEntity> findAll();
    ClientEntity findById(Long id);
    ClientEntity findByEmail(String email);
    void deleteById(Long id);
}
