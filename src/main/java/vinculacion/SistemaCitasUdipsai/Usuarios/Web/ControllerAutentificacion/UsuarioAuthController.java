package vinculacion.SistemaCitasUdipsai.Usuarios.Web.ControllerAutentificacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinculacion.SistemaCitasUdipsai.Usuarios.Services.UsuarioService;
import vinculacion.SistemaCitasUdipsai.Usuarios.Services.dto.RegisterDto;
import vinculacion.SistemaCitasUdipsai.Usuarios.Services.dto.RegistrarUsuarioDTO;
import vinculacion.SistemaCitasUdipsai.Usuarios.Services.dto.UsuarioDTO;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioEntity;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioAuthController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioAuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioEntity> saveUsuario(@RequestBody RegistrarUsuarioDTO usuarioRegister) {
        UsuarioEntity savedUsuario = usuarioService.registrarUsuario(usuarioRegister);
        return new ResponseEntity<>(savedUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<UsuarioEntity> getUsuarioByCedula(@PathVariable String cedula) {
        Optional<UsuarioEntity> usuario = usuarioService.findByCedula(cedula);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UsuarioDTO>> getAllUsuarios(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return usuarioService.obtenerUsuariosTodos(pageable);
    }

    @GetMapping("/roles/{cedula}")
    public ResponseEntity<List<String>> getUsuarioRolesByCedula(@PathVariable String cedula) {
        Optional<UsuarioEntity> usuarioOpt = usuarioService.findByCedula(cedula);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UsuarioEntity usuario = usuarioOpt.get();
        List<String> roles = usuario.getUsuarioRoles().stream()
                .filter(r -> r.getEstado() != null && r.getEstado().equals("A"))
                .map(UsuarioRolEntity::getRol)
                .map(rol -> rol.getNombre())
                .collect(Collectors.toList());
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<String> deleteUsuarioByCedula(@PathVariable String cedula) {
        return usuarioService.eliminarUsuario(cedula);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<Void> updateUsuario(@PathVariable String cedula, @RequestBody UsuarioEntity updatedUsuario) {
        if (!cedula.equals(updatedUsuario.getCedula())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioService.findByCedula(cedula);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Long id = usuarioOpt.get().getIdUsuario();
        try {
            usuarioService.actualizarUsuario(id, updatedUsuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}