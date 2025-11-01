package com.sabinomecanica.backend.controllers;


import com.sabinomecanica.backend.models.Servico;
import com.sabinomecanica.backend.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public List<Servico> buscarTodos() {
        return servicoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable UUID id) {
        return servicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Servico> cadastrar(@RequestBody Servico servico) {

        if (servico.getItemServicos() == null || servico.getItemServicos().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Servico novoServico = servicoService.salvar(servico);
        return new ResponseEntity<>(novoServico, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable UUID id, @RequestBody Servico servicoDetalhes) {
        return servicoService.buscarPorId(id).map(servicoExistente -> {

            servicoExistente.setData_ini(servicoDetalhes.getData_ini());
            servicoExistente.setData_fim(servicoDetalhes.getData_fim());
            servicoExistente.setDescricao(servicoDetalhes.getDescricao());

            // Nota: Atualizar os ItemServicos é mais complexo, exigiria mais lógica no Service
            // e normalmente se usa um DTO para evitar salvar a lista inteira novamente.
            // Para o básico, chamamos o salvar que recalcula o valor:
            Servico atualizado = servicoService.salvar(servicoExistente);
            return ResponseEntity.ok(atualizado);

        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (servicoService.buscarPorId(id).isPresent()) {
            servicoService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
