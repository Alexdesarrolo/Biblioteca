package org.desarrollo.libreria.web.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "editoriales")
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "direccion", nullable = false)
    @NotNull
    private String direccion;

    @Column(name = "nombre_completo", nullable = false, length = 90)
    @NotEmpty
    private String nombreCompleto;

    @Column(name = "pais")
    @NotEmpty
    private String pais;

    @Column(name = "url")
    @NotEmpty
    private String url;

    @OneToMany(mappedBy = "editorial", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Libro> libros;

    public Editorial() {
        libros = new ArrayList<> ();
    }

    public Editorial(Long id, String direccion, String nombreCompleto, String pais, String url) {
        this.id = id;
        this.direccion = direccion;
        this.nombreCompleto = nombreCompleto;
        this.pais = pais;
        this.url = url;
        libros = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Editorial [id=" + id + ", direccion=" + direccion + ", nombreCompleto=" + nombreCompleto + ", pais="
                + pais + ", url=" + url + "]";
    }


}
