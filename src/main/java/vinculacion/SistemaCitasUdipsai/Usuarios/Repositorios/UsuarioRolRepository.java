package vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolEntity;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolId;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, UsuarioRolId> {

}