package vinculacion.SistemaCitasUdipsai.Usuarios.Services;

import vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions.InvalidRequestBodyException;
import vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions.ResourceNotFoundException;
import vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions.UnauthorizedAccessException;
import vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios.AreaRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios.RolRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios.UsuarioRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.Services.dto.*;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private AreaRepository areaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    // Mapear de un Usuario a DTO.
    public UsuarioDTO mapearDTO(UsuarioEntity usr) {
        UsuarioDTO dto = new UsuarioDTO(
                usr.getIdUsuario(),
                usr.getCedula(),
                usr.getEstado(),
                usr.getNombres(),
                usr.getApellidos(),
                usr.getEmail(),
                usr.getCelular(),
                usr.getUsuarioRoles().stream()
                        .filter(usuarioRol -> usuarioRol.getEstado().equals("A"))
                        .map(usuarioRol -> {
                            RolEntity rol = usuarioRol.getRol();
                            RolDTO rolDTO = new RolDTO(
                                    rol.getIdRol(),
                                    rol.getNombre(),
                                    rol.getEstado());
                            return rolDTO;
                        }).collect(Collectors.toSet()),
                usr.getUsuarioAreas().stream()
                        .filter(usuarioArea -> usuarioArea.getEstado().equals("A"))
                        .map(usuarioArea -> {
                            AreaEntity area = usuarioArea.getArea();
                            AreaDTO areaDTO = new AreaDTO(
                                    area.getIdArea(),
                                    area.getNombre(),
                                    area.getEstado());
                            return areaDTO;
                        }).collect(Collectors.toSet()));
        return dto;
    }

    // Mapear de una lista de Usuarios a una lista de DTOs.
    public List<UsuarioDTO> mapearDTOs(List<UsuarioEntity> usuarios) {
        List<UsuarioDTO> dtos = usuarios
                .stream()
                .map(usr -> mapearDTO(usr))
                .toList();
        return dtos;
    }

    // Obtener todos los Usuarios activos.
    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuarios(Pageable pageable) {
        logger.info("obtenerUsuariosActivos()");
        logger.info("Obteniendo todos los usuarios activos");
        Page<UsuarioEntity> usuariosActivosPage = usuarioRepo.findAllByEstado("A", pageable);
        Page<UsuarioDTO> usuariosDTOs = usuariosActivosPage.map(usr -> mapearDTO(usr));
        logger.info("200 OK: Usuarios activos obtenidos correctamente");
        return new ResponseEntity<>(usuariosDTOs, HttpStatus.OK);
    }

    // Obtener un Usuario especifico.
    public ResponseEntity<UsuarioDTO> obtenerUsuario(String cedula) {
        logger.info("obtenerUsuario()");
        logger.info("Obteniendo usuario con cedula: " + cedula);
        UsuarioEntity usuario = usuarioRepo.findByCedula(cedula).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con cedula " + cedula + " no encontrado"));
        UsuarioDTO usuarioDTO = mapearDTO(usuario);
        logger.info("200 OK: Usuario obtenido correctamente");
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    // Obtener todos los Usuarios (A, I, B, N).
    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosTodos(Pageable pageable) {
        logger.info("obtenerUsuarios()");
        logger.info("Obteniendo todos los usuarios");
        Page<UsuarioEntity> usuariosPage = usuarioRepo.findAll(pageable);
        Page<UsuarioDTO> usuariosDTOs = usuariosPage.map(usr -> mapearDTO(usr));
        logger.info("200 OK: Usuarios obtenidos correctamente");
        return new ResponseEntity<>(usuariosDTOs, HttpStatus.OK);
    }

    // Obtener todos los Usuarios inactivos.
    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosInactivos(Pageable pageable) {
        logger.info("obtenerUsuariosInactivos()");
        logger.info("Obteniendo todos los usuarios inactivos");
        Page<UsuarioEntity> usuariosInactivosPage = usuarioRepo.findAllByEstado("I", pageable);
        Page<UsuarioDTO> usuariosDTOs = usuariosInactivosPage.map(usr -> mapearDTO(usr));
        logger.info("200 OK: Usuarios inactivos obtenidos correctamente");
        return new ResponseEntity<>(usuariosDTOs, HttpStatus.OK);
    }

    // Obtener todos los Usuarios bloqueados.
    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosBloqueados(Pageable pageable) {
        logger.info("obtenerUsuariosBloqueados()");
        logger.info("Obteniendo todos los usuarios bloqueados");
        Page<UsuarioEntity> usuariosBloqueadosPage = usuarioRepo.findAllByEstado("B", pageable);
        Page<UsuarioDTO> usuariosDTOs = usuariosBloqueadosPage.map(usr -> mapearDTO(usr));
        logger.info("200 OK: Usuarios bloqueados obtenidos correctamente");
        return new ResponseEntity<>(usuariosDTOs, HttpStatus.OK);
    }

    // Obtener todos los Usuarios eliminados.
    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosEliminados(Pageable pageable) {
        logger.info("obtenerUsuariosEliminados()");
        logger.info("Obteniendo todos los usuarios eliminados");
        Page<UsuarioEntity> usuariosEliminadosPage = usuarioRepo.findAllByEstado("N", pageable);
        Page<UsuarioDTO> usuariosDTOs = usuariosEliminadosPage.map(usr -> mapearDTO(usr));
        logger.info("200 OK: Usuarios eliminados obtenidos correctamente");
        return new ResponseEntity<>(usuariosDTOs, HttpStatus.OK);
    }

    // Registrar un Usuario nuevo.
    public UsuarioEntity registrarUsuario(RegistrarUsuarioDTO usuario) {
        logger.info("registrarUsuario()");
        logger.info("Registrando usuario con cedula {}", usuario.getCedula());

        if (usuario.getCedula() == null || usuario.getContrasenia() == null || usuario.getNombres() == null
                || usuario.getApellidos() == null || usuario.getEmail() == null || usuario.getCelular() == null) {
            throw new InvalidRequestBodyException("Faltan campos obligatorios para registrar el usuario");
        }

        UsuarioEntity usuarioNuevo = new UsuarioEntity();
        usuarioNuevo.setCedula(usuario.getCedula());
        usuarioNuevo.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        usuarioNuevo.setNombres(usuario.getNombres());
        usuarioNuevo.setApellidos(usuario.getApellidos());
        usuarioNuevo.setEmail(usuario.getEmail());
        usuarioNuevo.setCelular(usuario.getCelular());
        usuarioNuevo.setEstado("A");

        ZonedDateTime utc5 = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        LocalDateTime localDateTimeUTC5 = utc5.toLocalDateTime();
        usuarioNuevo.setFechaCreacion(localDateTimeUTC5);
        usuarioNuevo.setFechaModificacion(localDateTimeUTC5);

        Set<UsuarioRolEntity> usuarioRoles = usuario.getRoles().stream().map(rol -> {
            RolEntity rolEncontrado = rolRepo.findByNombre(rol.getNombre()).get();

            if (rolEncontrado != null && rolEncontrado.getEstado().equals("A")) {
                UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
                usuarioRol.setUsuario(usuarioNuevo);
                usuarioRol.setRol(rolEncontrado);
                usuarioRol.setFechaAsignacion(localDateTimeUTC5);
                usuarioRol.setEstado("A");
                return usuarioRol;
            } else {
                return null;
            }

        }).collect(Collectors.toSet());

        Set<UsuarioAreaEntity> usuarioAreas = usuario.getAreas().stream().map(area -> {
            AreaEntity areaEncontrada = areaRepo.findById(area.getIdArea()).get();

            if (areaEncontrada != null && areaEncontrada.getEstado().equals("A")) {
                UsuarioAreaEntity usuarioArea = new UsuarioAreaEntity();
                usuarioArea.setUsuario(usuarioNuevo);
                usuarioArea.setArea(areaEncontrada);
                usuarioArea.setFechaAsignacion(localDateTimeUTC5);
                usuarioArea.setEstado("A");
                return usuarioArea;
            } else {
                return null;
            }

        }).collect(Collectors.toSet());

        usuarioNuevo.setUsuarioRoles(usuarioRoles);
        usuarioNuevo.setUsuarioAreas(usuarioAreas);

        UsuarioEntity usuarioGuardado = usuarioRepo.save(usuarioNuevo);
        logger.info("Usuario registrado correctamente");
        return usuarioGuardado;
    }

    // Cambiar contraseña de un Usuario.
    public ResponseEntity<?> cambiarContrasenia(String cedula, CambiarContraseniaDTO peticion) {
        logger.info("cambiarContrasenia()");
        logger.info("Cambiando contraseña de usuario con cedula: " + cedula);
        UsuarioEntity usuarioEncontrado = usuarioRepo.findByCedula(cedula).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con cedula " + cedula + " no encontrado"));

        String antigua = peticion.getContrasenia();
        String nueva = peticion.getNuevaContrasenia();

        if (!passwordEncoder.matches(antigua, usuarioEncontrado.getContrasenia())) {
            throw new UnauthorizedAccessException("Contraseña antigua es incorrecta");
        }

        usuarioEncontrado.setContrasenia(passwordEncoder.encode(nueva));

        ZonedDateTime utc5 = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        LocalDateTime localDateTimeUTC5 = utc5.toLocalDateTime();
        usuarioEncontrado.setFechaModificacion(localDateTimeUTC5);

        usuarioRepo.save(usuarioEncontrado);
        logger.info("200 OK: Contraseña actualizada correctamente");
        logger.info("Fecha de modificacion: " + usuarioEncontrado.getFechaModificacion().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Contraseña actualizada correctamente");
        response.put("fechaModificacion", usuarioEncontrado.getFechaModificacion().toString());
        return ResponseEntity.ok(response);
    }

    public Optional<UsuarioEntity> findByCedula(String cedula) {
        return usuarioRepo.findByCedula(cedula);
}

    // Eliminar un Usuario.
    public ResponseEntity<String> eliminarUsuario(String cedula) {
        logger.info("eliminarUsuario()");
        logger.info("Eliminando usuario con cedula: " + cedula);
        UsuarioEntity usuarioEncontrado = usuarioRepo.findByCedula(cedula).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con cedula " + cedula + " no encontrado"));
        usuarioEncontrado.setEstado("N");

        Set<UsuarioRolEntity> usuarioRoles = usuarioEncontrado.getUsuarioRoles();
        for (UsuarioRolEntity usuarioRol : usuarioRoles) {
            usuarioRol.setEstado("N");
        }

        Set<UsuarioAreaEntity> usuarioAreas = usuarioEncontrado.getUsuarioAreas();
        for (UsuarioAreaEntity usuarioArea : usuarioAreas) {
            usuarioArea.setEstado("N");
        }

        usuarioRepo.save(usuarioEncontrado);
        logger.info("200 OK: Usuario eliminado correctamente");
        return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
    }

    // Habilitar un Usuario.
    public ResponseEntity<String> habilitarUsuario(String cedula) {
        logger.info("habilitarUsuario()");
        logger.info("Habilitando usuario con cedula: " + cedula);
        UsuarioEntity usuarioEncontrado = usuarioRepo.findByCedula(cedula).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con cedula " + cedula + " no encontrado"));
        usuarioEncontrado.setEstado("A");
        usuarioRepo.save(usuarioEncontrado);
        logger.info("200 OK: Usuario habilitado correctamente");
        return new ResponseEntity<>("Usuario activado correctamente", HttpStatus.OK);
    }

    // Deshabilitar un Usuario.
    public ResponseEntity<String> deshabilitarUsuario(String cedula) {
        logger.info("deshabilitarUsuario()");
        logger.info("Deshabilitando usuario con cedula: " + cedula);
        UsuarioEntity usuarioEncontrado = usuarioRepo.findByCedula(cedula).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con cedula " + cedula + " no encontrado"));
        usuarioEncontrado.setEstado("I");
        usuarioRepo.save(usuarioEncontrado);
        logger.info("200 OK: Usuario deshabilitado correctamente");
        return new ResponseEntity<>("Usuario desactivado correctamente", HttpStatus.OK);
    }

    // Bloquear un Usuario.
    public ResponseEntity<String> bloquearUsuario(String cedula) {
        logger.info("bloquearUsuario()");
        logger.info("Bloqueando usuario con cedula: " + cedula);
        UsuarioEntity usuarioEncontrado = usuarioRepo.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con cedula " + cedula + " no encontrado"));
        usuarioEncontrado.setEstado("B");
        usuarioRepo.save(usuarioEncontrado);
        logger.info("200 OK: Usuario bloqueado correctamente");
        return new ResponseEntity<>("Usuario bloqueado correctamente", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UsuarioEntity> actualizarUsuario(Long id, UsuarioEntity usuario) {
        logger.info("actualizarUsuario()");
        logger.info("Actualizando usuario con cedula: " + id);

        UsuarioEntity usuarioEncontrado = usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuarioEncontrado.setNombres(usuario.getNombres());
        usuarioEncontrado.setApellidos(usuario.getApellidos());
        usuarioEncontrado.setEmail(usuario.getEmail());
        usuarioEncontrado.setCelular(usuario.getCelular());

        if (usuarioEncontrado.getUsuarioRoles() != null) {
            usuarioEncontrado.getUsuarioRoles().forEach(usuarioRol -> usuarioRol.setEstado("N"));
        }
        if (usuarioEncontrado.getUsuarioAreas() != null) {
            usuarioEncontrado.getUsuarioAreas().forEach(usuarioArea -> usuarioArea.setEstado("N"));
        }

        ZonedDateTime utc5 = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        LocalDateTime localDateTimeUTC5 = utc5.toLocalDateTime();
        usuarioEncontrado.setFechaModificacion(localDateTimeUTC5);

        Set<UsuarioRolEntity> usuarioRolesNuevos = usuario.getUsuarioRoles().stream()
                .map(rol -> rolRepo.findByNombre(rol.getRol().getNombre()).orElse(null))
                .filter(Objects::nonNull)
                .filter(rol -> "A".equals(rol.getEstado()))
                .map(rol -> {
                    UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
                    usuarioRol.setUsuario(usuarioEncontrado);
                    usuarioRol.setRol(rol);
                    usuarioRol.setFechaAsignacion(localDateTimeUTC5);
                    usuarioRol.setEstado("A");
                    return usuarioRol;
                })
                .collect(Collectors.toSet());

        Set<UsuarioAreaEntity> usuarioAreasNuevos = usuario.getUsuarioAreas().stream()
                .map(area -> areaRepo.findById(area.getArea().getIdArea()).orElse(null))
                .filter(Objects::nonNull)
                .filter(area -> "A".equals(area.getEstado()))
                .map(area -> {
                    UsuarioAreaEntity usuarioArea = new UsuarioAreaEntity();
                    usuarioArea.setUsuario(usuarioEncontrado);
                    usuarioArea.setArea(area);
                    usuarioArea.setFechaAsignacion(localDateTimeUTC5);
                    usuarioArea.setEstado("A");
                    return usuarioArea;
                })
                .collect(Collectors.toSet());

        if (!usuarioRolesNuevos.isEmpty()) {
            usuarioEncontrado.setUsuarioRoles(usuarioRolesNuevos);
        } else {
            logger.error("Se registró el usuario sin ningún rol");
        }

        if (!usuarioAreasNuevos.isEmpty()) {
            usuarioEncontrado.setUsuarioAreas(usuarioAreasNuevos);
        } else {
            logger.error("Se registró el usuario sin ninguna área asignada");
        }

        UsuarioEntity usuarioGuardado = usuarioRepo.save(usuarioEncontrado);
        logger.info("Usuario actualizado correctamente");
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.OK);
    }
}

