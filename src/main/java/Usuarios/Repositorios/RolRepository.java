package com.udipsai.ms_usuarios.persistence.repository;

import com.udipsai.ms_usuarios.persistence.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Repositorio de la entidad Rol.
*/
@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {
    Optional<RolEntity> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<RolEntity> findAllByEstado(String estado);
}
