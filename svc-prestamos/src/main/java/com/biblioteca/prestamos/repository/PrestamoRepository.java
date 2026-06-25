package com.biblioteca.prestamos.repository;

import com.biblioteca.prestamos.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, String> {

    List<Prestamo> findByMiembroId(String miembroId);

    List<Prestamo> findByIsbn(String isbn);

    List<Prestamo> findByEstado(String estado);
}
