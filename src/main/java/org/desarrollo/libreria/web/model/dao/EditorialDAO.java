package org.desarrollo.libreria.web.model.dao;

import org.desarrollo.libreria.web.model.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialDAO extends JpaRepository<Editorial,Long>{
    
}
