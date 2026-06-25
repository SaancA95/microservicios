package com.biblioteca.miembros.controller;

import com.biblioteca.miembros.dto.MiembroDTO;
import com.biblioteca.miembros.model.Miembro;
import com.biblioteca.miembros.service.MiembrosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/miembros")
public class MiembrosController {

    @Autowired
    private MiembrosService miembrosService;

    /** GET /miembros/{id} — con caché Redis */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMiembroById(@PathVariable String id) {
        Map<String, Object> resultado = miembrosService.getMiembroById(id);
        if (resultado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Miembro no encontrado", "id", id));
        }
        return ResponseEntity.ok(resultado);
    }

    /** POST /miembros — registrar nuevo miembro */
    @PostMapping
    public ResponseEntity<Miembro> crearMiembro(@Valid @RequestBody MiembroDTO dto) {
        Miembro creado = miembrosService.crearMiembro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** GET /miembros/health */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "servicio", "svc-miembros",
            "estado", "UP",
            "puerto", "8093"
        ));
    }
}
