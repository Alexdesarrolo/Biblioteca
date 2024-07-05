package org.desarrollo.libreria.web.model.dao;

import org.desarrollo.libreria.web.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario,Long>{
    
}
