package vinculacion.SistemaCitasUdipsai.Usuarios.Web.ControllerAutentificacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinculacion.SistemaCitasUdipsai.Usuarios.Services.UsuarioRolService;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioRolEntity;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class UsuarioRolController {


    @Autowired
    private UsuarioRolService usuarioRolService;

    @GetMapping
    public List<UsuarioRolEntity> getAllUsuarioRoles() {
        return usuarioRolService.getAllUsuarioRoles();
    }

    @GetMapping("/{idUsuario}/{rol}")
    public ResponseEntity<UsuarioRolEntity> getUsuarioRolById(
            @PathVariable Long idUsuario,
            @PathVariable Long rol) {
        return usuarioRolService.getUsuarioRolById(idUsuario, rol)
                .map(usuarioRol -> new ResponseEntity<>(usuarioRol, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<UsuarioRolEntity> createUsuarioRol(@RequestBody UsuarioRolEntity usuarioRol) {
        UsuarioRolEntity createdUsuarioRol = usuarioRolService.createUsuarioRol(usuarioRol);
        return new ResponseEntity<>(createdUsuarioRol, HttpStatus.CREATED);
    }

    @PutMapping("/{idUsuario}/{rol}")
    public ResponseEntity<UsuarioRolEntity> updateUsuarioRol(
            @PathVariable Long idUsuario,
            @PathVariable Long rol,
            @RequestBody UsuarioRolEntity updatedUsuarioRol) {
        try {
            UsuarioRolEntity modifiedUsuarioRol = usuarioRolService.updateUsuarioRol(idUsuario, rol, updatedUsuarioRol);
            return new ResponseEntity<>(modifiedUsuarioRol, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idUsuario}/{rol}")
    public ResponseEntity<Void> deleteUsuarioRol(
            @PathVariable Long idUsuario,
            @PathVariable Long rol) {
        try {
            usuarioRolService.deleteUsuarioRol(idUsuario, rol);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}






