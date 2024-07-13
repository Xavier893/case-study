package com.xavier.client_backend.controllers;

import com.xavier.client_backend.domain.dto.ClientDto;
import com.xavier.client_backend.domain.entities.ClientEntity;
import com.xavier.client_backend.mappers.Mapper;
import com.xavier.client_backend.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final Mapper<ClientEntity, ClientDto> clientMapper;

    public ClientController(ClientService clientService, Mapper<ClientEntity, ClientDto> clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        ClientEntity clientEntity = clientMapper.mapFrom(clientDto);
        ClientEntity savedClientEntity = clientService.save(clientEntity);
        return new ResponseEntity<>(clientMapper.mapTo(savedClientEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> listClients() {
        List<ClientEntity> clients = clientService.findAll();
        List<ClientDto> clientDtos = clients.stream()
                .map(clientMapper::mapTo)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientDtos, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ClientDto> getCurrentClient(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        ClientEntity client = clientService.findByEmail(email);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ClientDto clientDto = clientMapper.mapTo(client);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientEntity client = clientService.findById(id);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ClientDto clientDto = clientMapper.mapTo(client);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
