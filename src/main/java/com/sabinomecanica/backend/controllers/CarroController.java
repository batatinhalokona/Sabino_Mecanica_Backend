package com.sabinomecanica.backend.controllers;

import com.sabinomecanica.backend.models.Carro;
import com.sabinomecanica.backend.services.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping
    public List<Carro> buscarTodos() {
        return carroService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> buscarPorId(@PathVariable UUID id) {
        return carroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Carro> cadastrar(@RequestBody Carro carro) {
        Carro novoCarro = carroService.salvar(carro);
        return new ResponseEntity<>(novoCarro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizar(@PathVariable UUID id, @RequestBody Carro carroDetalhes) {
        return carroService.buscarPorId(id).map(carroExistente -> {
            carroExistente.setModelo(carroDetalhes.getModelo());
            carroExistente.setPlaca(carroDetalhes.getPlaca());
            Carro atualizado = carroService.salvar(carroExistente);
            return ResponseEntity.ok(atualizado);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (carroService.buscarPorId(id).isPresent()) {
            carroService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

