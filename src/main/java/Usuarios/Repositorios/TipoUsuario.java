package Usuarios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoUsuario extends JpaRepository<TipoUsuario, Long> {
    TipoUsuario findByNombre(String nombre);
}
