package com.biblioteca.prestamos.controller;

import com.biblioteca.prestamos.dto.PrestamoDTO;
import com.biblioteca.prestamos.model.Prestamo;
import com.biblioteca.prestamos.service.PrestamosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/prestamos")
public class PrestamosController {

    @Autowired
    private PrestamosService prestamosService;

    /** POST /prestamos — crear préstamo */
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@Valid @RequestBody PrestamoDTO dto) {
        Prestamo prestamo = prestamosService.crearPrestamo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    /** GET /prestamos/{id} — con caché Redis */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrestamoById(@PathVariable String id) {
        Map<String, Object> resultado = prestamosService.getPrestamoById(id);
        if (resultado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Préstamo no encontrado", "id", id));
        }
        return ResponseEntity.ok(resultado);
    }

    /** PUT /prestamos/{id}/devolver */
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverPrestamo(@PathVariable String id) {
        Prestamo prestamo = prestamosService.devolverPrestamo(id);
        if (prestamo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Préstamo no encontrado", "id", id));
        }
        return ResponseEntity.ok(prestamo);
    }

    /** GET /prestamos/health */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "servicio", "svc-prestamos",
            "estado", "UP",
            "puerto", "8092"
        ));
    }
}
