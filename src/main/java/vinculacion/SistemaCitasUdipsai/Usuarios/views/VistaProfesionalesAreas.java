package vinculacion.SistemaCitasUdipsai.Usuarios.views;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Getter
@Setter
public class VistaProfesionalesAreas {

    @Id
    private Long idProfesional;

    private String especialidad;
    private String estado;
    private Long idArea;
    private String nombreArea;


    private Long idUsuario;

    private String cedula;

    private String email;

    private String celular;

    private String nombres;

    private String apellidos;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;


    // Getters y Setters
    // Opcional: Constructores, equals/hashCode, toString, etc.


}