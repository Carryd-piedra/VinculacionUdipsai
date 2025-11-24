package vinculacion.SistemaCitasUdipsai.Exceptions;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.error("404 NOT FOUND: Recurso no encontrado");
        logger.error(ex.getMessage());

        ExceptionResponse body = new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "ResourceNotFound",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequestBody(InvalidRequestBodyException ex) {
        logger.error("400 BAD REQUEST: Cuerpo de la petición inválido");
        logger.error(ex.getMessage());

        ExceptionResponse body = new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "InvalidRequestBody",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        logger.error("401 UNAUTHORIZED: Acceso no autorizado");
        logger.error(ex.getMessage());

        ExceptionResponse body = new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                "UnauthorizedAccess",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ExceptionResponse> handleDataConflict(DataConflictException ex) {
        logger.error("409 CONFLICT: Conflicto de datos");
        logger.error(ex.getMessage());

        ExceptionResponse body = new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                "DataConflict",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ExceptionResponse> handleNumberFormatException(NumberFormatException ex) {
        logger.error("400 BAD REQUEST: Formato de número inválido");
        logger.error(ex.getMessage());

        ExceptionResponse body = new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "NumberFormatException",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";
        String error = String.format("El parámetro '%s' en la ruta %s debería ser del tipo '%s'", ex.getName(),
                request.getDescription(false),
                requiredType);
        logger.error("400 BAD REQUEST: Formato de parámetro inválido");
        logger.error(error);

        ExceptionResponse body = new ExceptionResponse(
                error,
                HttpStatus.BAD_REQUEST.value(),
                "MethodArgumentTypeMismatch",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = "URL no encontrada en el servidor";
        logger.error("404 NOT FOUND: {}", message);

        ExceptionResponse body = new ExceptionResponse(
                message,
                HttpStatus.NOT_FOUND.value(),
                "NoHandlerFound",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerError(Exception ex) {
        logger.error("500 INTERNAL SERVER ERROR: Error en el servidor", ex);

        ExceptionResponse body = new ExceptionResponse(
                "Error en el servidor",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "InternalServerError",
                LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
