package org.desarrollo.libreria.web.model.dao;

import org.desarrollo.libreria.web.model.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoDAO extends JpaRepository<Prestamo,Long>{
    
    @Query("select p from Prestamo p join fetch p.usuario u join fetch p.ejemplar e join fetch e.libro l where p.idPrestamo = ?1")
    public Prestamo buscarPorIdConUsuarioConEjemplaresConLibro(Long idPrestamo);
}
