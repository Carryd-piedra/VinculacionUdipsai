package vinculacion.SistemaCitasUdipsai.Exceptions;

public class InvalidRequestBodyException extends RuntimeException {
    public InvalidRequestBodyException(String message) {
        super(message);
    }
}
