package org.desarrollo.libreria.web.model.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "ejemplares")
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Column(name = "numero_ejemplar")
    private Integer numeroEjemplar;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id")
    private Libro libro;
    
    //@Transient
    private String  titulo;
    
    //@Transient
    private String nombreAutor;
    
    private boolean activo;
    
    public Ejemplar() {
    }

    public Ejemplar(Long id, boolean estado, Date fechaIngreso, Integer numeroEjemplar) {
        this.id = id;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.numeroEjemplar = numeroEjemplar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getNumeroEjemplar() {
        return numeroEjemplar;
    }

    public void setNumeroEjemplar(Integer numeroEjemplar) {
        this.numeroEjemplar = numeroEjemplar;
    }
    
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    
    public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	

	public String getNombreAutor() {
		return nombreAutor;
	}

	public void setNombreAutor(String nombreAutor) {
		this.nombreAutor = nombreAutor;
	}

	@Override
    public String toString() {
        return "Ejemplar [id=" + id + ", estado=" + estado + ", fechaIngreso=" + fechaIngreso + ", numeroEjemplar="
                + numeroEjemplar + "]";
    }

}
