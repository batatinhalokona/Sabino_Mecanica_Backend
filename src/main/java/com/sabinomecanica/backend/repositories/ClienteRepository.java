package com.sabinomecanica.backend.repositories;

import com.sabinomecanica.backend.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    void deleteById(UUID id);

    Optional<Cliente> findById(UUID id);
}
