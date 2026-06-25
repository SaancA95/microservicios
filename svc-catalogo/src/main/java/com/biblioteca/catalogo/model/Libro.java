package com.biblioteca.catalogo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "libros")
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "genero")
    private String genero;

    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    @Column(name = "disponible")
    private Boolean disponible;

    @Column(name = "copias_totales")
    private Integer copiasTotales;

    @Column(name = "copias_disponibles")
    private Integer copiasDisponibles;

    public Libro() {}

    public Libro(String isbn, String titulo, String autor, String genero,
                 Integer anioPublicacion, Boolean disponible,
                 Integer copiasTotales, Integer copiasDisponibles) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.disponible = disponible;
        this.copiasTotales = copiasTotales;
        this.copiasDisponibles = copiasDisponibles;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(Integer anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public Integer getCopiasTotales() { return copiasTotales; }
    public void setCopiasTotales(Integer copiasTotales) { this.copiasTotales = copiasTotales; }

    public Integer getCopiasDisponibles() { return copiasDisponibles; }
    public void setCopiasDisponibles(Integer copiasDisponibles) { this.copiasDisponibles = copiasDisponibles; }
}
