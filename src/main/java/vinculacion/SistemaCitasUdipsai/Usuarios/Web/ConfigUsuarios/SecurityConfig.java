package vinculacion.SistemaCitasUdipsai.Usuarios.Web.ConfigUsuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final LogFilter logFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter, LogFilter logFilter) {
        this.jwtFilter = jwtFilter;
        this.logFilter = logFilter;
    }

    private static final String[] WHITE_LIST_URL = {
            "/api/hello/**",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private static final String[] POST_LIST_URL = {
            "/api/secretarias",
            "/api/coordinadores",
            "/api/profesionales",
            "/api/pasantes",
            "/api/areas",
            "/api/roles"
    };

    private static final String[] GET_LIST_URL = {
            "/api/usuarios/**",
            "/api/secretarias/**",
            "/api/coordinadores/**",
            "/api/profesionales/**",
            "/api/pasantes/**",
            "/api/areas/**",
            "/api/roles/**"
    };

    private static final String[] PATCH_LIST_URL = {
            "/api/usuarios/habilitar/**",
            "/api/usuarios/deshabilitar/**",
            "/api/usuarios/bloquear/**",
            "/api/secretarias/habilitar/**",
            "/api/secretarias/deshabilitar/**",
            "/api/secretarias/bloquear/**",
            "/api/coordinadores/habilitar/**",
            "/api/coordinadores/deshabilitar/**",
            "/api/coordinadores/bloquear/**",
            "/api/profesionales/habilitar/**",
            "/api/profesionales/deshabilitar/**",
            "/api/profesionales/bloquear/**",
            "/api/pasantes/habilitar/**",
            "/api/pasantes/deshabilitar/**",
            "/api/pasantes/**",
            "/api/areas/habilitar/**",
            "/api/areas/deshabilitar/**",
            "/api/areas/bloquear/**",
            "/api/roles/habilitar/**",
            "/api/roles/deshabilitar/**",
            "/api/roles/bloquear/**"
    };

    private static final String[] DELETE_LIST_URL = {
            "/api/usuarios/**",
            "/api/secretarias/**",
            "/api/coordinadores/**",
            "/api/areas/**",
            "/api/roles/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(logFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and()
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(POST, "/api/admins").permitAll()
                                .requestMatchers(GET, "/api/admins/**").hasAnyRole("ADMIN")
                                .requestMatchers("/api/areas/**").permitAll()
                                .requestMatchers(PUT, "/api/admins/**").hasAnyRole("ADMIN")
                                .requestMatchers(PATCH, "/api/admins/**").hasAnyRole("ADMIN")
                                .requestMatchers(DELETE, "/api/admins/**").hasAnyRole("ADMIN")
                                .requestMatchers(POST, "/api/pasantes").hasAnyRole("PROFESIONAL")
                                .requestMatchers(POST, POST_LIST_URL).hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR")
                                .requestMatchers(GET, GET_LIST_URL)
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR", "PROFESIONAL")
                                .requestMatchers(PUT, "/api/secretarias/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR")
                                .requestMatchers(PUT, "/api/coordinadores/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR")
                                .requestMatchers(PUT, "/api/profesionales/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR", "PROFESIONAL")
                                .requestMatchers(PUT, "/api/pasantes/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR", "PROFESIONAL", "PASANTE")
                                .requestMatchers(PATCH, "/api/usuarios/cambiarContrasenia/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR", "PROFESIONAL", "PASANTE")
                                .requestMatchers(PATCH, "/api/pasantes/**").hasAnyRole("PROFESIONAL")
                                .requestMatchers(PATCH, PATCH_LIST_URL).hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR")
                                .requestMatchers(DELETE, DELETE_LIST_URL)
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR")
                                .requestMatchers(DELETE, "/api/profesionales/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR")
                                .requestMatchers(DELETE, "/api/pasantes/**")
                                .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR", "PROFESIONAL")
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        return http.build();
    }
}
