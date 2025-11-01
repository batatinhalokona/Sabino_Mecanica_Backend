package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.ItemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemServicoRepository extends JpaRepository<ItemServico, UUID> {

    List<ItemServico> findByServico_Id(UUID idServico);

    Optional<ItemServico> findById(UUID id);

    void deleteById(UUID id);
}
