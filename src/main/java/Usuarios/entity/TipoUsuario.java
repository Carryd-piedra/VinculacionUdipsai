package Usuarios.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
