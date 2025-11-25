package vinculacion.SistemaCitasUdipsai.Usuarios.Services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions.ResourceNotFoundException;
import vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios.UsuarioRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioSecurityService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioSecurityService.class);

    @Autowired
    public UsuarioSecurityService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        logger.info("loadUserByUsername()");
        logger.info("Cargando usuario con cédula " + cedula);
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con cédula " + cedula + " no encontrado"));

        String[] roles = usuarioEntity.getUsuarioRoles().stream()
                .filter(usuarioRol -> usuarioRol.getRol().getEstado().equals("A"))
                .map(usuarioRol -> usuarioRol.getRol().getNombre())
                .toArray(String[]::new);

        logger.info("Usuario encontrado");

        return User.builder()
                .username(usuarioEntity.getCedula())
                .password(usuarioEntity.getContrasenia())
                .authorities(this.grantedAuthorities(roles))
                .disabled("I".equals(usuarioEntity.getEstado()))
                .accountLocked("B".equals(usuarioEntity.getEstado()))
                .build();
    }

    private List<GrantedAuthority> grantedAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        for (String rol : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));
        }

        return authorities;
    }
}
