package vinculacion.SistemaCitasUdipsai.Usuarios.Web.ConfigUsuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
            "/v3/api-docs/**",

            
            // white list de citas

            "/api/pacientes/**",
            "/api/citas/**",
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Filtros
                .addFilterBefore(logFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Reglas de acceso
                .authorizeHttpRequests(auth -> auth

                        // ---------------------
                        // ENDPOINTS PÚBLICOS
                        // ---------------------
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/**",
                                "/actuator/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Registrar primer usuario (opcional)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers("/api/usuarios/**").permitAll()

                        // ---------------------
                        // REGLAS DE ANIMALES (del primer config)
                        // ---------------------
                        .requestMatchers("/api/animals/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST, "/api/animals/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/animals/**").hasRole("ADMIN")

                        // ---------------------
                        // REGLAS DE ADOPCIONES Y CLIENTES
                        // ---------------------
                        .requestMatchers("/api/adoptions/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN","USER")

                        // ---------------------
                        // REGLAS DE ÁREAS
                        // ---------------------
                        .requestMatchers("/api/areas/**").permitAll()

                        // ---------------------
                        // ADMINISTRADORES
                        // ---------------------
                        .requestMatchers(HttpMethod.POST, "/api/admins").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/admins/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admins/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/admins/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admins/**").hasRole("ADMIN")

                        // ---------------------
                        // SECRETARIAS / COORDINADORES / PROFESIONALES
                        // ---------------------
                        .requestMatchers(HttpMethod.PUT, "/api/secretarias/**")
                        .hasAnyRole("ADMIN","SECRETARIA","COORDINADOR")

                        .requestMatchers(HttpMethod.PUT, "/api/coordinadores/**")
                        .hasAnyRole("ADMIN","SECRETARIA","COORDINADOR")

                        .requestMatchers(HttpMethod.PUT, "/api/profesionales/**")
                        .hasAnyRole("ADMIN","SECRETARIA","COORDINADOR","PROFESIONAL")

                        // ---------------------
                        // PASANTES
                        // ---------------------
                        .requestMatchers(HttpMethod.POST, "/api/pasantes")
                        .hasAnyRole("PROFESIONAL")

                        .requestMatchers(HttpMethod.PUT, "/api/pasantes/**")
                        .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR","PROFESIONAL","PASANTE")

                        .requestMatchers(HttpMethod.PATCH, "/api/pasantes/**")
                        .hasRole("PROFESIONAL")

                        .requestMatchers(HttpMethod.DELETE, "/api/pasantes/**")
                        .hasAnyRole("ADMIN", "SECRETARIA", "COORDINADOR","PROFESIONAL")

                        // ---------------------
                        // ACTUALIZAR CONTRASEÑA
                        // ---------------------
                        .requestMatchers(HttpMethod.PATCH, "/api/usuarios/cambiarContrasenia/**")
                        .hasAnyRole("ADMIN","SECRETARIA","COORDINADOR","PROFESIONAL","PASANTE")

                        // ---------------------
                        // CUALQUIER OTRO REQUIERE AUTENTICACIÓN
                        // ---------------------
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
