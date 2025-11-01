package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, UUID> {
    Optional<Servico> findById(UUID id);

    void deleteById(UUID id);
}
