package com.biblioteca.catalogo.repository;

import com.biblioteca.catalogo.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {

    List<Libro> findByAutor(String autor);

    List<Libro> findByGenero(String genero);

    List<Libro> findByDisponibleTrue();
}
