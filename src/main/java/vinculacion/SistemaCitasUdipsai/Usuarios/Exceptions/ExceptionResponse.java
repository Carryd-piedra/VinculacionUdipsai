package vinculacion.SistemaCitasUdipsai.Usuarios.Exceptions;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
}
