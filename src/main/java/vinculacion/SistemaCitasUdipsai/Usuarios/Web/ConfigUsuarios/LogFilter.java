package vinculacion.SistemaCitasUdipsai.Usuarios.Web.ConfigUsuarios;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LogFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("========== REQUEST DATA ==========");
        logger.info("Origin IP=[{}]", request.getRemoteAddr());
        logger.info("URI=[{}]", request.getRequestURI());
        logger.info("Method=[{}]", request.getMethod());
        logger.info("Host Header=[{}]", request.getHeader("Host"));
        logger.info("Server IP=[{}]", request.getLocalAddr());
        logger.info("Content-Type Header=[{}]", request.getHeader("Content-Type"));
        logger.info("User-Agent Header=[{}]", request.getHeader("User-Agent"));

        filterChain.doFilter(request, response);
    }
}
