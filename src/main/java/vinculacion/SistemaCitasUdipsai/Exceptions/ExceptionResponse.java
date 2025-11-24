package vinculacion.SistemaCitasUdipsai.Exceptions;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
}
