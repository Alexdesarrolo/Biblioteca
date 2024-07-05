package org.desarrollo.libreria.web.model.dao;

import org.desarrollo.libreria.web.model.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorDAO extends JpaRepository<Autor,Long>{
    
}
