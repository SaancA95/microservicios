package com.biblioteca.prestamos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "prestamos")
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "miembro_id", nullable = false)
    private String miembroId;

    @Column(name = "fecha_prestamo")
    private String fechaPrestamo;

    @Column(name = "fecha_devolucion_estimada")
    private String fechaDevolucionEstimada;

    @Column(name = "fecha_devolucion_real")
    private String fechaDevolucionReal;

    @Column(name = "estado")
    private String estado;

    public Prestamo() {}

    public Prestamo(String isbn, String miembroId) {
        this.id = "PRE-" + System.currentTimeMillis();
        this.isbn = isbn;
        this.miembroId = miembroId;
        this.fechaPrestamo = new Date().toString();
        this.fechaDevolucionEstimada = new Date(System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000).toString();
        this.fechaDevolucionReal = null;
        this.estado = "ACTIVO";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getMiembroId() { return miembroId; }
    public void setMiembroId(String miembroId) { this.miembroId = miembroId; }

    public String getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public String getFechaDevolucionEstimada() { return fechaDevolucionEstimada; }
    public void setFechaDevolucionEstimada(String v) { this.fechaDevolucionEstimada = v; }

    public String getFechaDevolucionReal() { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(String fechaDevolucionReal) { this.fechaDevolucionReal = fechaDevolucionReal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
