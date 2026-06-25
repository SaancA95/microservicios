package com.biblioteca.miembros.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MiembroDTO {

    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    private String email;

    @NotBlank(message = "El tipo de miembro es obligatorio")
    @Pattern(regexp = "ESTUDIANTE|DOCENTE|EXTERNO",
             message = "Tipo debe ser: ESTUDIANTE, DOCENTE o EXTERNO")
    private String tipoMiembro;

    public MiembroDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoMiembro() { return tipoMiembro; }
    public void setTipoMiembro(String tipoMiembro) { this.tipoMiembro = tipoMiembro; }
}
