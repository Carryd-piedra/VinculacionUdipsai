package Usuarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UsuarioRolId.class)
@Table(name = "usuarios_roles")
public class UsuarioRolEntity {
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuarioEntity;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rol;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    // A activo
    // I deshabilitado
    // N borrado
    // B bloqueado
    @Column(name = "estado", length = 5, nullable = false)
    private String estado;
}
