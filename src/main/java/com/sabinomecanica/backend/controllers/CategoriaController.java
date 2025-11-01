package com.sabinomecanica.backend.controllers;


import com.sabinomecanica.backend.models.Categoria;
import com.sabinomecanica.backend.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> buscarTodos() {
        return categoriaService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable UUID id) {
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categoria> cadastrar(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.salvar(categoria);
        return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable UUID id, @RequestBody Categoria categoriaDetalhes) {
        return categoriaService.buscarPorId(id).map(categoriaExistente -> {
            categoriaExistente.setDescricao(categoriaDetalhes.getDescricao());
            Categoria atualizada = categoriaService.salvar(categoriaExistente);
            return ResponseEntity.ok(atualizada);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (categoriaService.buscarPorId(id).isPresent()) {
            categoriaService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
