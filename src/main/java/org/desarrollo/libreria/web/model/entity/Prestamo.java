package org.desarrollo.libreria.web.model.entity;


import java.util.Calendar;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Long idPrestamo;

    @Column(name = "estado", nullable = false)
    @NotNull
    private String estado;

    @Column(name = "fecha_alquiler", nullable = false)
    private Date fechaAlquiler;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    @Column(name = "fecha_devolucion", nullable = false)
    private Date fechaDevolucion;

    @Column(name = "observacion", nullable = false)
    private String observacion;
    
    @Column(name = "diferencia_fechas", nullable = false)
    private Long diferenciaFechas;

    @ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejemplar_id")
    private Ejemplar ejemplar;
    
    

    public Prestamo() {
    }
    

    public Prestamo(String estado, Date fechaAlquiler, Date fechaDevolucion, String observacion,
			Long diferenciaFechas, Usuario usuario, Ejemplar ejemplar) {
		super();
		this.estado = estado;
		this.fechaAlquiler = fechaAlquiler;
		this.fechaDevolucion = fechaDevolucion;
		this.observacion = observacion;
		this.diferenciaFechas = diferenciaFechas;
		this.usuario = usuario;
		this.ejemplar = ejemplar;
	}


	public Prestamo(Long idPrestamo, String estado, Date fechaAlquiler, Date fechaDevolucion, String observacion) {
        this.idPrestamo = idPrestamo;
        this.estado = estado;
        this.fechaAlquiler = fechaAlquiler;
        this.fechaDevolucion = fechaDevolucion;
        this.observacion = observacion;
    }
	
	public Long diferenciaDias() {
		// Crear objetos Calendar y establecer las fechas
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaDevolucion);

        // Obtener la diferencia entre las fechas
        long diferencia = cal2.getTimeInMillis() - cal1.getTimeInMillis(); //Convertimos las dos fechas en milisegundos

        // Calcular el resto de días
        long diasRestantes = (diferencia+(24 * 60 * 60 * 1000)) / (24 * 60 * 60 * 1000); //calcula el número de días restantes a partir de la diferencia en milisegundos entre dos fechas
    	diferenciaFechas = diasRestantes;
    	
    	return diferenciaFechas;
	}
	
    public Long getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Long idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaAlquiler() {
        return fechaAlquiler;
    }

    public void setFechaAlquiler(Date fechaAlquiler) {
        this.fechaAlquiler = fechaAlquiler;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }
    
    public void addEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
	}

    public boolean isFechaDevuelto() {
        Date fechaActual = new Date();
        return fechaActual.after(fechaDevolucion);
    }
    
    public Long getDiferenciaFechas() {
		return diferenciaFechas;
	}

	public void setDiferenciaFechas(Long diferenciaFechas) {
		this.diferenciaFechas = diferenciaFechas;
	}

	@Override
    public String toString() {
        return "Prestamo [idPrestamo=" + idPrestamo + ", estado=" + estado + ", fechaAlquiler=" + fechaAlquiler
                + ", fechaDevolucion=" + fechaDevolucion + ", observacion=" + observacion + "]";
    }


}
