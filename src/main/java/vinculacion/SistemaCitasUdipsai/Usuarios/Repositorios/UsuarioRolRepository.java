package vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolEntity;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolId;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, UsuarioRolId> {
}
