package org.desarrollo.libreria.web.model.dao;

import java.util.List;

import org.desarrollo.libreria.web.model.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroDAO  extends JpaRepository<Libro,Long>{
    
    @Query("select l from Libro l where l.titulo like %?1%")
	public List<Libro> buscarPorTitulo(String term);
}
