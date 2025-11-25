package vinculacion.SistemaCitasUdipsai.Usuarios.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false, unique = true)
    private Long idUsuario;

    @Column(name = "cedula", length = 12, nullable = false, unique = true)
    private String cedula;

    @Column(name = "contrasenia", length = 200, nullable = false)
    private String contrasenia;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "celular", nullable = true)
    private String celular;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @Column(name = "estado", length = 5, nullable = false)
    private String estado;

    // ROLES
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UsuarioRolEntity> usuarioRoles;

    // AREAS
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UsuarioAreaEntity> usuarioAreas;
}
