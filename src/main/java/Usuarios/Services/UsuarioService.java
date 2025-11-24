package Usuarios.Services;

import Usuarios.Repositorios.UsuarioRepository;
import Usuarios.entity.Usuario;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public List<Usuario> listarPorTipo(String tipo) {
        return usuarioRepository.findByTipoUsuarioNombre(tipo);
    }
}

