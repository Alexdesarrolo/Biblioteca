package org.desarrollo.libreria.web.model.services;

import java.util.List;

import org.desarrollo.libreria.web.model.dao.AutorDAO;
import org.desarrollo.libreria.web.model.dao.EditorialDAO;
import org.desarrollo.libreria.web.model.dao.EjemplarDAO;
import org.desarrollo.libreria.web.model.dao.LibroDAO;
import org.desarrollo.libreria.web.model.dao.PrestamoDAO;
import org.desarrollo.libreria.web.model.dao.UsuarioDAO;
import org.desarrollo.libreria.web.model.entity.Autor;
import org.desarrollo.libreria.web.model.entity.Editorial;
import org.desarrollo.libreria.web.model.entity.Ejemplar;
import org.desarrollo.libreria.web.model.entity.Libro;
import org.desarrollo.libreria.web.model.entity.Prestamo;
import org.desarrollo.libreria.web.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibreriaServiceImpl implements LibreriaService{

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private LibroDAO libroDAO;

    @Autowired
    private PrestamoDAO prestamoDAO;

	@Autowired
	private EditorialDAO editorialDAO;

	@Autowired
	private AutorDAO autorDAO;

	@Autowired
	private EjemplarDAO ejemplarDAO;
    // servicios para Usuario
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> buscarUsuariosTodos() {
		return (List<Usuario>) usuarioDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> buscarUsuariosTodos(Pageable pageable) {
		return usuarioDAO.findAll(pageable);
	}
	
	@Override
	@Transactional
	public void actualizarUsuario(Usuario usuario) {
		usuarioDAO.save(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario buscarUsuarioPorId(Long id) {
		return usuarioDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarUsuarioPorId(Long id) {
		usuarioDAO.deleteById(id);
	}

    // Servicios para Libro
    @Override
	@Transactional(readOnly = true)
	public List<Libro> buscarLibrosTodos() {
		return (List<Libro>) libroDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Libro> buscarLibrosTodos(Pageable pageable) {
		return libroDAO.findAll(pageable);
	}
	
	@Override
	@Transactional
	public void actualizarLibro(Libro libro) {
		libroDAO.save(libro);
	}

	@Override
	@Transactional(readOnly = true)
	public Libro buscarLibroPorId(Long idLibro) {
		return libroDAO.findById(idLibro).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarLibroPorId(Long idLibro) {
		libroDAO.deleteById(idLibro);
	}

		

    // servicios para Prestamo
	@Override
	@Transactional(readOnly = true)
	public Page<Prestamo> buscarPrestamosTodos(Pageable pageable) {
		actualizarDiasRestantes();
		return prestamoDAO.findAll(pageable);
	}
	
	
	@Override
	@Transactional
	public void guardarPrestamo(Prestamo prestamo) {
		prestamoDAO.save(prestamo);
	}

	@Override
	@Transactional(readOnly = true)
	public Prestamo buscarPrestamoPorIdPrestamo(Long idPrestamo){
		return prestamoDAO.findById(idPrestamo).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarPrestamo(Long idPrestamo){
		prestamoDAO.deleteById(idPrestamo);
	}

	
	@Override
	@Transactional(readOnly = true)
	public Prestamo buscarPrestamoPorIdConUsuarioConEjemplarConLibro(Long idPrestamo) {
		return prestamoDAO.buscarPorIdConUsuarioConEjemplaresConLibro(idPrestamo);
	}
	public void actualizarDiasRestantes() {
		List<Prestamo> prestamos = prestamoDAO.findAll();
		for(Prestamo p: prestamos) {
			p.diferenciaDias();
		}
	}
	

	// Servicios para Editorial
	@Override
	@Transactional(readOnly = true)
	public List<Editorial> listarTodasLasEditoriales() {
		return (List<Editorial>) editorialDAO.findAll();
	}

	// Servicios para Autor
	@Override
	@Transactional(readOnly = true)
	public List<Autor> listarTodosLosAutores() {
		return (List<Autor>) autorDAO.findAll();
	}

	// Servicios para Ejemplar
	@Override
	@Transactional(readOnly = true)
	public Ejemplar buscarEjemplarPorId(Long id) {
		return ejemplarDAO.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Ejemplar> buscarEjemlaresTodos() {
		return (List<Ejemplar>) ejemplarDAO.findAll();
	}

	
	
	@Override
	@Transactional
	public void actualizarEjemplar(Ejemplar ejemplar) {
		ejemplarDAO.save(ejemplar);
	}


	@Override
	@Transactional
	public void eliminarEjemplarPorId(Long id) {
		ejemplarDAO.deleteById(id);
	}
	@Override
	@Transactional(readOnly = true)
	public Page<Ejemplar> buscarEjemlaresTodos(Pageable pageable) {
		return ejemplarDAO.findAll(pageable);
		
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Ejemplar> buscarLibrosPorTitulo(String term) {
		return ejemplarDAO.buscarPorTitulo(term);
	}




}
