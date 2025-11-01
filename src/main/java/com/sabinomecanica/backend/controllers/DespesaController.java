package com.sabinomecanica.backend.controllers;


import com.sabinomecanica.backend.models.Despesa;
import com.sabinomecanica.backend.services.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public List<Despesa> buscarTodos() {
        return despesaService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarPorId(@PathVariable UUID id) {
        return despesaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Despesa> cadastrar(@RequestBody Despesa despesa) {
        Despesa novaDespesa = despesaService.salvar(despesa);
        return new ResponseEntity<>(novaDespesa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despesa> atualizar(@PathVariable UUID id, @RequestBody Despesa despesaDetalhes) {
        return despesaService.buscarPorId(id).map(despesaExistente -> {
            despesaExistente.setData(despesaDetalhes.getData());
            despesaExistente.setValor(despesaDetalhes.getValor());
            despesaExistente.setDescricao(despesaDetalhes.getDescricao());
            despesaExistente.setCategoria(despesaDetalhes.getCategoria()); // Associa a categoria

            Despesa atualizada = despesaService.salvar(despesaExistente);
            return ResponseEntity.ok(atualizada);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (despesaService.buscarPorId(id).isPresent()) {
            despesaService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
