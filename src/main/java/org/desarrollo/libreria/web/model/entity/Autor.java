package org.desarrollo.libreria.web.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutor;

    @Column(name = "nombre", length = 90, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 90, nullable = false)
    private String apellido;

    @Column(name = "pais", nullable = false)
    private String pais;
    
    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros = new ArrayList<>();

    public Autor() {
    }

    public Autor(Long idAutor, String nombre, String apellido, String pais) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public void addLibro(Libro libros) {
        this.libros.add(libros);
    }

   

    @Override
    public String toString() {
        return "Autor [idAutor=" + idAutor + ", nombre=" + nombre + ", apellido=" + apellido + ", pais=" + pais
                + ", libros=" + libros + "]";
    }

    

    
}
