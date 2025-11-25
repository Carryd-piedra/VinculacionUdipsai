package Usuarios.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAreaId implements Serializable {
    private Long usuario;
    private Long area;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UsuarioAreaId that = (UsuarioAreaId) o;
        return Objects.equals(usuario, that.usuario) && Objects.equals(area, that.area);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, area);
    }
}