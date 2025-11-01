package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.MovtoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovtoEntradaRepository extends JpaRepository<MovtoEntrada, UUID> {
    Optional<MovtoEntrada> findById(UUID id);

    void deleteById(UUID id);
}
