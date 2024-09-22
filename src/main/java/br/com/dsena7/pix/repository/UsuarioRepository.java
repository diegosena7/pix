package br.com.dsena7.pix.repository;

import br.com.dsena7.pix.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {
    UserDetails findByLogin(String login);
}
