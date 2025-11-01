package com.sabinomecanica.backend.services;

import com.sabinomecanica.backend.models.ItemServico;
import com.sabinomecanica.backend.models.Servico;
import com.sabinomecanica.backend.repositories.ItemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemServicoService {

    @Autowired
    private ItemServicoRepository itemServicoRepository;

    @Autowired
    private ServicoService servicoService;

    public List<ItemServico> buscarTodos() {
        return itemServicoRepository.findAll();
    }

    public Optional<ItemServico> buscarPorId(UUID id) {
        return itemServicoRepository.findById(id);
    }

    public List<ItemServico> buscarPorServico(UUID idServico) {
        return itemServicoRepository.findByServico_Id(idServico);
    }

    @Transactional
    public ItemServico salvar(ItemServico item) {
        // Lógica de Negócio: Garantir que quantidade e valor unitário são positivos
        if (item.getQuantidade() <= 0 || item.getValor_unitario() <= 0) {
            throw new IllegalArgumentException("Quantidade e valor unitário devem ser maiores que zero.");
        }

        // 1. Salva o item
        ItemServico itemSalvo = itemServicoRepository.save(item);

        // 2. Recalcula o valor total do Serviço pai (crucial)
        if (itemSalvo.getServico() != null) {
            // Buscamos o Servico completo para garantir que todos os itens sejam considerados no recálculo
            Optional<Servico> servicoPaiOpt = servicoService.buscarPorId(itemSalvo.getServico().getId());

            servicoPaiOpt.ifPresent(servicoPai -> {
                // Chama o método que lida com o recálculo e o salvamento do Servico
                servicoService.recalcularESalvar(servicoPai);
            });
        }

        return itemSalvo;
    }

    @Transactional
    public void deletar(UUID id) {
        // 1. Busca o item para obter a referência do Serviço pai
        Optional<ItemServico> itemOpt = itemServicoRepository.findById(id);

        if (itemOpt.isPresent()) {
            UUID servicoId = itemOpt.get().getServico().getId();

            // 2. Deleta o item
            itemServicoRepository.deleteById(id);

            // 3. Recalcula o valor total do Serviço pai
            Optional<Servico> servicoPaiOpt = servicoService.buscarPorId(servicoId);
            servicoPaiOpt.ifPresent(servicoPai -> {
                servicoService.recalcularESalvar(servicoPai);
            });
        }
    }
}