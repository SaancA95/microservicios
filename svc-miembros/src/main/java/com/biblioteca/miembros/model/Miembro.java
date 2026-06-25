package com.biblioteca.miembros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "miembros")
public class Miembro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "tipo_miembro")
    private String tipoMiembro;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    @Column(name = "prestamos_activos")
    private Integer prestamosActivos;

    public Miembro() {}

    public Miembro(String id, String nombre, String email, String tipoMiembro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoMiembro = tipoMiembro;
        this.fechaRegistro = new Date().toString();
        this.prestamosActivos = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoMiembro() { return tipoMiembro; }
    public void setTipoMiembro(String tipoMiembro) { this.tipoMiembro = tipoMiembro; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Integer getPrestamosActivos() { return prestamosActivos; }
    public void setPrestamosActivos(Integer prestamosActivos) { this.prestamosActivos = prestamosActivos; }
}
