package com.sabinomecanica.backend.controllers;


import com.sabinomecanica.backend.models.MovtoEntrada;
import com.sabinomecanica.backend.services.MovtoEntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movtos-entrada") // URL alterada
public class MovtoEntradaController {

    @Autowired
    private MovtoEntradaService movtoEntradaService; // Alterado

    // GET /api/movtos-entrada
    @GetMapping
    public List<MovtoEntrada> buscarTodos() { // Alterado
        return movtoEntradaService.buscarTodos();
    }

    // GET /api/movtos-entrada/1
    @GetMapping("/{id}")
    public ResponseEntity<MovtoEntrada> buscarPorId(@PathVariable UUID id) { // Alterado
        return movtoEntradaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/movtos-entrada
    @PostMapping
    public ResponseEntity<MovtoEntrada> cadastrar(@RequestBody MovtoEntrada movimento) { // Alterado
        try {
            MovtoEntrada novoMovimento = movtoEntradaService.salvar(movimento); // Alterado
            return new ResponseEntity<>(novoMovimento, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Se a validação do service falhar (ex: valor negativo)
            return ResponseEntity.badRequest().body(null);
        }
    }

    // PUT /api/movtos-entrada/1
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @RequestBody MovtoEntrada movimentoDetalhes) { // Alterado
        return movtoEntradaService.buscarPorId(id).map(movimentoExistente -> {

            // Atualiza os campos relevantes
            movimentoExistente.setData(movimentoDetalhes.getData());
            movimentoExistente.setValor_pago(movimentoDetalhes.getValor_pago());
            movimentoExistente.setServico(movimentoDetalhes.getServico()); // Atualiza o serviço se necessário

            try {
                MovtoEntrada atualizado = movtoEntradaService.salvar(movimentoExistente); // Alterado
                return ResponseEntity.ok(atualizado);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/movtos-entrada/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (movtoEntradaService.buscarPorId(id).isPresent()) {
            movtoEntradaService.deletar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
