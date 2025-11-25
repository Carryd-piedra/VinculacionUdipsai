package vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCedula(String cedula);

    Page<UsuarioEntity> findAll(Pageable pageable);

    Page<UsuarioEntity> findAllByNombres(String nombres, Pageable pageable);

    Page<UsuarioEntity> findAllByApellidos(String apellidos, Pageable pageable);

    Page<UsuarioEntity> findAllByEstado(String estado, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByCedula(String cedula);
}
