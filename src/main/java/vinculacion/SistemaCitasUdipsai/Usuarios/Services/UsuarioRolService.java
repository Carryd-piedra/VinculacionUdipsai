package vinculacion.SistemaCitasUdipsai.Usuarios.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios.UsuarioRolRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolEntity;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolId;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioRolService {

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    public List<UsuarioRolEntity> getAllUsuarioRoles() {
        return usuarioRolRepository.findAll();
    }

    public Optional<UsuarioRolEntity> getUsuarioRolById(Long idUsuario, Long rol) {
        return usuarioRolRepository.findById(new UsuarioRolId(idUsuario, rol));
    }

    public UsuarioRolEntity createUsuarioRol(UsuarioRolEntity usuarioRol) {
        return usuarioRolRepository.save(usuarioRol);
    }

    public UsuarioRolEntity updateUsuarioRol(Long idUsuario, Long rol, UsuarioRolEntity updatedUsuarioRol) {
        Optional<UsuarioRolEntity> existingUsuarioRol = usuarioRolRepository.findById(new UsuarioRolId(idUsuario, rol));
        if (existingUsuarioRol.isPresent()) {
            UsuarioRolEntity usuarioRolEntity = existingUsuarioRol.get();
            usuarioRolEntity.setFechaAsignacion(updatedUsuarioRol.getFechaAsignacion());
            return usuarioRolRepository.save(usuarioRolEntity);
        } else {
            throw new RuntimeException("UsuarioRol not found");
        }
    }

    public void deleteUsuarioRol(Long idUsuario, Long rol) {
        usuarioRolRepository.deleteById(new UsuarioRolId(idUsuario, rol));
    }
}
