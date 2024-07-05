package org.desarrollo.libreria.web.model.dao;

import java.util.List;

import org.desarrollo.libreria.web.model.entity.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarDAO extends JpaRepository<Ejemplar,Long>{
    
	@Query("select l from Ejemplar l where l.libro.titulo like %?1%")
	public List<Ejemplar> buscarPorTitulo(String term);
}
