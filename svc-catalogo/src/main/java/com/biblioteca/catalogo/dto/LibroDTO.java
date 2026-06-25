package com.biblioteca.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Setter
@Getter
public class LibroDTO {

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    private String genero;

    @Positive(message = "El año debe ser positivo")
    private Integer anioPublicacion;

    @NotNull(message = "Disponible es obligatorio")
    private Boolean disponible;

    @Positive(message = "Las copias totales deben ser positivas")
    private Integer copiasTotales;

    @NotNull(message = "Las copias disponibles son obligatorias")
    private Integer copiasDisponibles;

    public LibroDTO() {}

}
