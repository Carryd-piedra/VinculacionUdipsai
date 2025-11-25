package Usuarios.Services;

import Usuarios.Repositorios.UsuarioRepository;
import Usuarios.entity.UsuarioEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioEntity guardar(UsuarioEntity usuarioEntity) {
        return usuarioRepository.save(usuarioEntity);
    }

    public List<UsuarioEntity> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioEntity> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public List<UsuarioEntity> listarPorTipo(String tipo) {
        return usuarioRepository.findByTipoUsuarioNombre(tipo);
    }
}

