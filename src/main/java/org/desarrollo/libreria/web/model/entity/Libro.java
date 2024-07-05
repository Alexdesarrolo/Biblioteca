package org.desarrollo.libreria.web.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;

    @Column(name = "titulo", length = 90, nullable = false)
    private String titulo;

    @Column(name = "edicion", length = 90, nullable = false)
    private String edicion;

    @Column(name = "año", length = 4, nullable = false)
    private int year;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "caratula")
    private String caratula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Editorial editorial;

    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "libros_autores",
        joinColumns = @JoinColumn(name = "idLibro"),
        inverseJoinColumns = @JoinColumn(name = "idAutor")
    )
    private List<Autor> autores = new ArrayList<>();
    
    public Libro() {
    }

    public Libro(Long idLibro, String titulo, String edicion, int year, String isbn, String caratula,
            Editorial editorial, List<Autor> autores) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.edicion = edicion;
        this.year = year;
        this.isbn = isbn;
        this.caratula = caratula;
        this.editorial = editorial;
        this.autores = autores;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public Editorial getEditorial() {
        return editorial;
    }


    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public List<Autor> getAutores() {
        return autores;
    }


    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    
    @Override
    public String toString() {
        return "Libro [id=" + idLibro + ", titulo=" + titulo + ", edicion=" + edicion + ", year=" + year + ", isbn=" + isbn
                + ", caratula=" + caratula + "]";
    }

    
}
