package com.xavier.client_backend.repositories;

import com.xavier.client_backend.domain.entities.ClientEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {
    ClientEntity findByEmail(String email);
}
