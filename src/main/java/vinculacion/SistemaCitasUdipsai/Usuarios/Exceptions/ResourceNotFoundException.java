package vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
