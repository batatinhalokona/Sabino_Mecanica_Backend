package com.sabinomecanica.backend.services;


import com.sabinomecanica.backend.models.Despesa;
import com.sabinomecanica.backend.repositories.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public List<Despesa> buscarTodos() {
        return despesaRepository.findAll();
    }

    public Optional<Despesa> buscarPorId(UUID id) {
        return despesaRepository.findById(id);
    }

    public Despesa salvar(Despesa despesa) {
        // Lógica de Negócio: Validação se valor > 0, etc.
        return despesaRepository.save(despesa);
    }

    public void deletar(UUID id) {
        despesaRepository.deleteById(id);
    }
}