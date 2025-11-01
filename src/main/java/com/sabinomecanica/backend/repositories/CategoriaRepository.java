package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findById(UUID id);

    void deleteById(UUID id);
}
