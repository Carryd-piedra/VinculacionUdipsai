package Usuarios.entity;

import lombok.*;

import jakarta.persistence.*;

/*
 * Clase que representa un Pasante en la capa de persistencia.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pasantes")
public class PasanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasante", nullable = false, unique = true)
    private Long idPasante;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "carrera", nullable = false)
    private String carrera;

    // A activo
    // I deshabilitado
    // N borrado
    // B bloqueado
    @Column(name = "estado", length = 5, nullable = false)
    private String estado;
}
