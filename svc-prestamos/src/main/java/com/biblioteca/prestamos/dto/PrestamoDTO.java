package com.biblioteca.prestamos.dto;

import jakarta.validation.constraints.NotBlank;

public class PrestamoDTO {

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotBlank(message = "El ID de miembro es obligatorio")
    private String miembroId;

    public PrestamoDTO() {}

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getMiembroId() { return miembroId; }
    public void setMiembroId(String miembroId) { this.miembroId = miembroId; }
}
