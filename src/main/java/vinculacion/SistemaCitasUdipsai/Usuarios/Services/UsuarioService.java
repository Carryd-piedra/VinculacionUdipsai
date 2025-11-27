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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Service
@RequiredArgsConstructor // Inyección de dependencias segura (Mejor práctica que @Autowired)
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final AreaRepository areaRepo;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    // ==========================================
    // MAPPERS (Conversión Entidad <-> DTO)
    // ==========================================

    public UsuarioDTO mapearDTO(UsuarioEntity usr) {
        return new UsuarioDTO(
                usr.getIdUsuario(),
                usr.getCedula(),
                usr.getEstado(),
                usr.getNombres(),
                usr.getApellidos(),
                usr.getEmail(),
                usr.getCelular(),
                usr.getUsuarioRoles().stream()
                        .filter(ur -> "A".equals(ur.getEstado()))
                        .map(ur -> new RolDTO(ur.getRol().getIdRol(), ur.getRol().getNombre(), ur.getRol().getEstado()))
                        .collect(Collectors.toSet()),
                usr.getUsuarioAreas().stream()
                        .filter(ua -> "A".equals(ua.getEstado()))
                        .map(ua -> new AreaDTO(ua.getArea().getIdArea(), ua.getArea().getNombre(), ua.getArea().getEstado()))
                        .collect(Collectors.toSet())
        );
    }

    public List<UsuarioDTO> mapearDTOs(List<UsuarioEntity> usuarios) {
        return usuarios.stream().map(this::mapearDTO).toList();
    }

    // ==========================================
    // MÉTODOS DE CONSULTA (GET)
    // ==========================================

    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuarios(Pageable pageable) {
        logger.info("Obteniendo usuarios activos");
        return ResponseEntity.ok(usuarioRepo.findAllByEstado("A", pageable).map(this::mapearDTO));
    }

    public ResponseEntity<UsuarioDTO> obtenerUsuario(String cedula) {
        logger.info("Obteniendo usuario: {}", cedula);
        UsuarioEntity usuario = usuarioRepo.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con cédula " + cedula + " no encontrado"));
        return ResponseEntity.ok(mapearDTO(usuario));
    }

    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosTodos(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepo.findAll(pageable).map(this::mapearDTO));
    }

    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosInactivos(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepo.findAllByEstado("I", pageable).map(this::mapearDTO));
    }

    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosBloqueados(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepo.findAllByEstado("B", pageable).map(this::mapearDTO));
    }

    public ResponseEntity<Page<UsuarioDTO>> obtenerUsuariosEliminados(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepo.findAllByEstado("N", pageable).map(this::mapearDTO));
    }

    // ==========================================
    // MÉTODOS TRANSACCIONALES (POST, PUT, DELETE)
    // ==========================================

    @Transactional
    public UsuarioEntity registrarUsuario(RegistrarUsuarioDTO usuario) {
        logger.info("Registrando usuario: {}", usuario.getCedula());

        if (usuario.getCedula() == null || usuario.getContrasenia() == null || usuario.getNombres() == null) {
            throw new InvalidRequestBodyException("Faltan campos obligatorios");
        }

        // Verificar si ya existe
        if(usuarioRepo.findByCedula(usuario.getCedula()).isPresent()){
            throw new InvalidRequestBodyException("El usuario con esa cédula ya existe");
        }

        UsuarioEntity nuevo = new UsuarioEntity();
        nuevo.setCedula(usuario.getCedula());
        nuevo.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        nuevo.setNombres(usuario.getNombres());
        nuevo.setApellidos(usuario.getApellidos());
        nuevo.setEmail(usuario.getEmail());
        nuevo.setCelular(usuario.getCelular());
        nuevo.setEstado("A");

        LocalDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
        nuevo.setFechaCreacion(ahora);
        nuevo.setFechaModificacion(ahora);

        // Asignación segura de Roles
        Set<UsuarioRolEntity> roles = usuario.getRoles().stream()
                .map(rolDto -> rolRepo.findByNombre(rolDto.getNombre())
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + rolDto.getNombre())))
                .filter(rol -> "A".equals(rol.getEstado()))
                .map(rol -> {
                    UsuarioRolEntity ur = new UsuarioRolEntity();
                    ur.setUsuario(nuevo);
                    ur.setRol(rol);
                    ur.setFechaAsignacion(ahora);
                    ur.setEstado("A");
                    return ur;
                }).collect(Collectors.toSet());

        // Asignación segura de Áreas
        Set<UsuarioAreaEntity> areas = usuario.getAreas().stream()
                .map(areaDto -> areaRepo.findById(areaDto.getIdArea())
                        .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada ID: " + areaDto.getIdArea())))
                .filter(area -> "A".equals(area.getEstado()))
                .map(area -> {
                    UsuarioAreaEntity ua = new UsuarioAreaEntity();
                    ua.setUsuario(nuevo);
                    ua.setArea(area);
                    ua.setFechaAsignacion(ahora);
                    ua.setEstado("A");
                    return ua;
                }).collect(Collectors.toSet());

        nuevo.setUsuarioRoles(roles);
        nuevo.setUsuarioAreas(areas);

        return usuarioRepo.save(nuevo);
    }

    @Transactional
    public ResponseEntity<UsuarioEntity> actualizarUsuario(Long id, UsuarioEntity usuarioDto) {
        logger.info("Actualizando usuario ID: {}", id);

        UsuarioEntity existente = usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        existente.setNombres(usuarioDto.getNombres());
        existente.setApellidos(usuarioDto.getApellidos());
        existente.setEmail(usuarioDto.getEmail());
        existente.setCelular(usuarioDto.getCelular());

        LocalDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
        existente.setFechaModificacion(ahora);

        // Desactivar relaciones anteriores
        if (existente.getUsuarioRoles() != null) existente.getUsuarioRoles().forEach(ur -> ur.setEstado("N"));
        if (existente.getUsuarioAreas() != null) existente.getUsuarioAreas().forEach(ua -> ua.setEstado("N"));

        // Crear nuevas relaciones
        // Nota: Asumimos que usuarioDto trae la estructura compleja de roles/areas.
        // Si no, deberías usar un DTO específico para update.
        if (usuarioDto.getUsuarioRoles() != null) {
            Set<UsuarioRolEntity> nuevosRoles = usuarioDto.getUsuarioRoles().stream()
                    .map(ur -> rolRepo.findByNombre(ur.getRol().getNombre()).orElse(null))
                    .filter(Objects::nonNull)
                    .map(rol -> {
                        UsuarioRolEntity ur = new UsuarioRolEntity();
                        ur.setUsuario(existente);
                        ur.setRol(rol);
                        ur.setFechaAsignacion(ahora);
                        ur.setEstado("A");
                        return ur;
                    }).collect(Collectors.toSet());
            existente.setUsuarioRoles(nuevosRoles);
        }

        if (usuarioDto.getUsuarioAreas() != null) {
            Set<UsuarioAreaEntity> nuevasAreas = usuarioDto.getUsuarioAreas().stream()
                    .map(ua -> areaRepo.findById(ua.getArea().getIdArea()).orElse(null))
                    .filter(Objects::nonNull)
                    .map(area -> {
                        UsuarioAreaEntity ua = new UsuarioAreaEntity();
                        ua.setUsuario(existente);
                        ua.setArea(area);
                        ua.setFechaAsignacion(ahora);
                        ua.setEstado("A");
                        return ua;
                    }).collect(Collectors.toSet());
            existente.setUsuarioAreas(nuevasAreas);
        }

        return ResponseEntity.ok(usuarioRepo.save(existente));
    }

    public ResponseEntity<?> cambiarContrasenia(String cedula, CambiarContraseniaDTO peticion) {
        UsuarioEntity usuario = usuarioRepo.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(peticion.getContrasenia(), usuario.getContrasenia())) {
            throw new UnauthorizedAccessException("Contraseña antigua incorrecta");
        }

        usuario.setContrasenia(passwordEncoder.encode(peticion.getNuevaContrasenia()));
        usuario.setFechaModificacion(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime());
        usuarioRepo.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada"));
    }

    // ==========================================
    // MÉTODOS DE ESTADO (Enable/Disable/Block)
    // ==========================================

    private ResponseEntity<String> cambiarEstadoUsuario(String cedula, String nuevoEstado, String mensajeExito) {
        UsuarioEntity usuario = usuarioRepo.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setEstado(nuevoEstado);

        // Si se elimina (N), eliminamos lógicamente sus relaciones también
        if("N".equals(nuevoEstado)){
            if(usuario.getUsuarioRoles() != null) usuario.getUsuarioRoles().forEach(r -> r.setEstado("N"));
            if(usuario.getUsuarioAreas() != null) usuario.getUsuarioAreas().forEach(a -> a.setEstado("N"));
        }

        usuarioRepo.save(usuario);
        return ResponseEntity.ok(mensajeExito);
    }

    public ResponseEntity<String> eliminarUsuario(String cedula) {
        return cambiarEstadoUsuario(cedula, "N", "Usuario eliminado correctamente");
    }

    public ResponseEntity<String> habilitarUsuario(String cedula) {
        return cambiarEstadoUsuario(cedula, "A", "Usuario activado correctamente");
    }

    public ResponseEntity<String> deshabilitarUsuario(String cedula) {
        return cambiarEstadoUsuario(cedula, "I", "Usuario desactivado correctamente");
    }

    public ResponseEntity<String> bloquearUsuario(String cedula) {
        return cambiarEstadoUsuario(cedula, "B", "Usuario bloqueado correctamente");
    }
}