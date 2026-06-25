package com.biblioteca.catalogo.controller;

import com.biblioteca.catalogo.dto.LibroDTO;
import com.biblioteca.catalogo.model.Libro;
import com.biblioteca.catalogo.service.CatalogoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/libros/{isbn}")
    public ResponseEntity<?> getLibroPorIsbn(@PathVariable String isbn) {
        Map<String, Object> resultado = catalogoService.getLibroPorIsbn(isbn);
        if (resultado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Libro no encontrado", "isbn", isbn));
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/libros")
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody LibroDTO dto) {
        Libro guardado = catalogoService.guardarLibro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping("/catalogo/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "servicio", "svc-catalogo",
                "estado", "UP",
                "puerto", "8091"
        ));
    }
}