package com.sabinomecanica.backend.controllers;

import com.sabinomecanica.backend.models.Cliente;
import com.sabinomecanica.backend.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodos() {
        List<Cliente> clientes = clienteService.buscarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorCpf(@PathVariable UUID id) {
        return clienteService.buscarPorCpf(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {

        if (cliente.getEndereco() == null) {
            return ResponseEntity.badRequest().build();
        }

        Cliente novoCliente = clienteService.salvar(cliente);

        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable UUID id, @RequestBody Cliente clienteDetalhes) {
        return clienteService.buscarPorCpf(id).map(clienteExistente -> {

            clienteExistente.setNome(clienteDetalhes.getNome());
            clienteExistente.setTelefone(clienteDetalhes.getTelefone());

            if (clienteDetalhes.getEndereco() != null) {
                clienteExistente.setEndereco(clienteDetalhes.getEndereco());
            }

            Cliente clienteAtualizado = clienteService.salvar(clienteExistente);
            return ResponseEntity.ok(clienteAtualizado);

        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (clienteService.buscarPorCpf(id).isPresent()) {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

