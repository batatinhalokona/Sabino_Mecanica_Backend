package com.sabinomecanica.backend.services;


import com.sabinomecanica.backend.models.Categoria;
import com.sabinomecanica.backend.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(UUID id) {
        return categoriaRepository.findById(id);
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deletar(UUID id) {
        categoriaRepository.deleteById(id);
    }
}
