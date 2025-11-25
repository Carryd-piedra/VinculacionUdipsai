package Usuarios.entity;

import lombok.*;

import jakarta.persistence.*;

/*
 * Clase que representa una Area en la capa de persistencia.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "areas")
public class AreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Long idArea;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    // A activo
    // I deshabilitado
    // N borrado
    // B bloqueado
    @Column(name = "estado", length = 5, nullable = false)
    private String estado;

}
