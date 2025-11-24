package Usuarios.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    private boolean activo = true;
}
