package org.desarrollo.libreria.web.model.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identificacion", nullable = false)
    private String identificacion;

    @Column(name = "nombres", length = 90, nullable = false)
    private String nombre;

    @Column(name = "apellidos", length = 90, nullable = false)
    private String apellido;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "telefono", length = 13)
    private String telefono;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent
	@Temporal(TemporalType.DATE)
	@NotNull
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Column(name = "foto")
    private String foto;

    private boolean activo;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Prestamo> prestamos;

    @Column(name = "ejemplares_vencidos")
    private int ejemplaresVencidos;


    public Usuario() {
        prestamos = new ArrayList<>();
    }

    public Usuario(Long id, String identificacion, String nombre, String apellido, String correoElectronico,
            String telefono, Date fechaIngreso, String foto, boolean activo) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.fechaIngreso = fechaIngreso;
        this.foto = foto;
        this.activo = activo;
        prestamos = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public void addPrestamo(Prestamo prestamo){
        this.prestamos.add(prestamo);
    }

    public void incrementarEjemplaresVencidos() {
        ejemplaresVencidos++;
    }
    

    public int getEjemplaresVencidos() {
        return ejemplaresVencidos;
    }

    public void setEjemplaresVencidos(int ejemplaresVencidos) {
        this.ejemplaresVencidos = ejemplaresVencidos;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", identificacion=" + identificacion + ", nombre=" + nombre + ", apellido="
                + apellido + ", correoElectronico=" + correoElectronico + ", telefono=" + telefono + ", fechaIngreso="
                + fechaIngreso + ", foto=" + foto + ", activo=" + activo + "]";
    }

    

    
}
