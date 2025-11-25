package Usuarios.entity;

import lombok.*;

import jakarta.persistence.*;

/*
 * Clase que representa un Administrador en la capa de persistencia.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin", nullable = false, unique = true)
    private Long idAdmin;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    // A activo
    // I deshabilitado
    // N borrado
    // B bloqueado
    @Column(name = "estado", length = 5, nullable = false)
    private String estado;
}
