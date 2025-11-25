package vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions;

public class InvalidRequestBodyException extends RuntimeException {
    public InvalidRequestBodyException(String message) {
        super(message);
    }
}
