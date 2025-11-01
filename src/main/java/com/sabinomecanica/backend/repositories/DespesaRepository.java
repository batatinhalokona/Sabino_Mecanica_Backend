package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, UUID> {
    Optional<Despesa> findById(UUID id);

    void deleteById(UUID id);
}
