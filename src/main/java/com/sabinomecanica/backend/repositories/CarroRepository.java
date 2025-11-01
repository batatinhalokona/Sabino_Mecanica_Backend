package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarroRepository extends JpaRepository<Carro, UUID> {
    Optional<Carro> findById(UUID id);
    void deleteById(UUID id);
}
