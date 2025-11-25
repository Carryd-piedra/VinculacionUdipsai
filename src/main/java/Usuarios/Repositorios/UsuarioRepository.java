package Usuarios.Repositorios;

import Usuarios.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCorreo(String correo);
    List<UsuarioEntity> findByTipoUsuarioNombre(String nombre);
}
