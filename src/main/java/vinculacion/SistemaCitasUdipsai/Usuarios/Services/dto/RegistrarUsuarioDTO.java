package vinculacion.SistemaCitasUdipsai.Usuarios.Services.dto;

import vinculacion.SistemaCitasUdipsai.Usuarios.entity.AreaEntity;
import vinculacion.SistemaCitasUdipsai.Usuarios.entity.RolEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
 * Clase que representa el registro de un Usuario en la capa de transferencia de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarUsuarioDTO {
    private String cedula;
    private String contrasenia;
    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private Set<RolEntity> roles;
    private Set<AreaEntity> areas;
}