package org.desarrollo.libreria.web.model.services;

import java.util.List;

import org.desarrollo.libreria.web.model.entity.Autor;
import org.desarrollo.libreria.web.model.entity.Editorial;
import org.desarrollo.libreria.web.model.entity.Ejemplar;
import org.desarrollo.libreria.web.model.entity.Libro;
import org.desarrollo.libreria.web.model.entity.Prestamo;
import org.desarrollo.libreria.web.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LibreriaService {
    
    // Servicios para Usuario
	
	public List<Usuario> buscarUsuariosTodos();
	public Page<Usuario> buscarUsuariosTodos(Pageable pageable);
	public void actualizarUsuario(Usuario usuario);
	public Usuario buscarUsuarioPorId(Long id);
	public void eliminarUsuarioPorId(Long id);
    

    // Servicios para Libro
	public List<Libro> buscarLibrosTodos();
	public Page<Libro> buscarLibrosTodos(Pageable pageable);
	public void actualizarLibro(Libro libro);
	public Libro buscarLibroPorId(Long idLibro);
	public void eliminarLibroPorId(Long idLibro);

	//public List<Libro> buscarLibrosPorTitulo(String term);	

    // Servicios para Prestamo
	public Page<Prestamo> buscarPrestamosTodos(Pageable pageable);
	public void guardarPrestamo(Prestamo prestamo);
	public Prestamo buscarPrestamoPorIdPrestamo(Long idPrestamo);
	public void eliminarPrestamo(Long idPrestamo);
	public Prestamo buscarPrestamoPorIdConUsuarioConEjemplarConLibro(Long idPrestamo);

	// Servicios para Editorial
	public List<Editorial> listarTodasLasEditoriales();

	// Servicios para Autor
	public List<Autor> listarTodosLosAutores();

	// Servicios para Ejemplar
   
	public List<Ejemplar> buscarEjemlaresTodos();
	public Page<Ejemplar> buscarEjemlaresTodos(Pageable pageable);
	public void actualizarEjemplar(Ejemplar usuario);
	public Ejemplar buscarEjemplarPorId(Long id);
	public void eliminarEjemplarPorId(Long id);
	
	public List<Ejemplar> buscarLibrosPorTitulo(String term);
}
