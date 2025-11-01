package com.sabinomecanica.backend.services;


import com.sabinomecanica.backend.models.ItemServico;
import com.sabinomecanica.backend.models.Servico;
import com.sabinomecanica.backend.repositories.ItemServicoRepository;
import com.sabinomecanica.backend.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ItemServicoRepository itemServicoRepository;

    public List<Servico> buscarTodos() {
        return servicoRepository.findAll();
    }

    public Optional<Servico> buscarPorId(UUID id) {
        return servicoRepository.findById(id);
    }

    @Transactional
    public Servico salvar(Servico servico) {

        double valorTotal = servico.getItemServicos().stream()
                .mapToDouble(ItemServico::getValor_unitario)
                .sum();
        servico.setValor_total(valorTotal);


        Servico savedServico = servicoRepository.save(servico);


        for (ItemServico item : savedServico.getItemServicos()) {
            item.setServico(savedServico);
            itemServicoRepository.save(item);
        }

        return savedServico;
    }

    public void deletar(UUID id) {
        servicoRepository.deleteById(UUID.fromString(String.valueOf(id)));
    }

    @Transactional
    public Servico recalcularESalvar(Servico servico) {

        // 1. Recarrega os itens
        // OBS: O ItemServicoRepository deve estar injetado na classe ServicoService
        List<ItemServico> itensAtuais = itemServicoRepository.findByServico_Id(servico.getId());

        // 2. Lógica de Recálculo (Usando BigDecimal para precisão)
        BigDecimal valorTotalDecimal = BigDecimal.ZERO;

        if (itensAtuais != null && !itensAtuais.isEmpty()) {
            valorTotalDecimal = itensAtuais.stream()
                    // PASSO 1: Mapear cada item para o seu subtotal (valor_unitario * quantidade)
                    .map(item -> {
                        // Obtém o valor unitário como BigDecimal
                        BigDecimal valorUnitario = BigDecimal.valueOf(item.getValor_unitario()); // Usando getValorUnitario

                        // Converte a quantidade (Integer) para BigDecimal para a multiplicação
                        BigDecimal quantidade = new BigDecimal(item.getQuantidade());

                        // Calcula o subtotal e retorna
                        return valorUnitario.multiply(quantidade);
                    })
                    // PASSO 2: Somar todos os subtotais em um único BigDecimal
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // 3. Atualiza e Salva

        // Converte o resultado de BigDecimal para double (Necessário para o seu Servico.setValor_total)
        double valorTotal = valorTotalDecimal.doubleValue();

        // Chama o setter do Servico, usando o nome que você indicou
        servico.setValor_total(valorTotal);

        return servicoRepository.save(servico);
    }
}