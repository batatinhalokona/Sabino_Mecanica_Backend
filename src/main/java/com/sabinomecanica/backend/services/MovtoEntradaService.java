package com.sabinomecanica.backend.services;

import com.sabinomecanica.backend.models.MovtoEntrada;
import com.sabinomecanica.backend.repositories.MovtoEntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovtoEntradaService {
    @Autowired
    private MovtoEntradaRepository movimentoEntradaRepository;

    public List<MovtoEntrada> buscarTodos() {
        return movimentoEntradaRepository.findAll();
    }

    public Optional<MovtoEntrada> buscarPorId(UUID id) {
        return movimentoEntradaRepository.findById(id);
    }

    @Transactional
    public MovtoEntrada salvar(MovtoEntrada movimento) {
        // Lógica de Negócio:
        // 1. Você pode querer validar se o valorPago é positivo.
        if (movimento.getValor_pago() == 0 || movimento.getValor_pago() <= 0) {
            // Em um projeto real, você lançaria uma exceção aqui, como InvalidDataException
            throw new IllegalArgumentException("O valor pago deve ser maior que zero.");
        }

        // 2. Você poderia adicionar lógica para verificar se o ID do Servico é válido.

        return movimentoEntradaRepository.save(movimento);
    }

    public void deletar(UUID id) {
        movimentoEntradaRepository.deleteById(id);
    }
}

