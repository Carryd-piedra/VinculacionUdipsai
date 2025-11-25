package vinculacion.SistemaCitasUdipsai.Usuarios.Web.ConfigUsuarios;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Permitir todos los orígenes
        corsConfiguration.addAllowedOriginPattern("*");

        // Métodos permitidos
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));

        // Encabezados permitidos
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

        // Exponer encabezados específicos
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));

        // Permitir credenciales
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}