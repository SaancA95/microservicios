package com.biblioteca.miembros.repository;

import com.biblioteca.miembros.model.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, String> {

    Optional<Miembro> findByEmail(String email);

    List<Miembro> findByTipoMiembro(String tipoMiembro);
}
