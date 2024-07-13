package com.xavier.client_backend.services.impl;

import com.xavier.client_backend.domain.entities.ClientEntity;
import com.xavier.client_backend.repositories.ClientRepository;
import com.xavier.client_backend.services.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientEntity save(ClientEntity clientEntity) {
        return clientRepository.save(clientEntity);
    }

    @Override
    public List<ClientEntity> findAll() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public ClientEntity findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ClientEntity findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
