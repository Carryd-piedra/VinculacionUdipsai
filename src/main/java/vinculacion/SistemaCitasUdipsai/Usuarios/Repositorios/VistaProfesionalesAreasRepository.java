package vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios;

import Usuarios.views.VistaProfesionalesAreas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VistaProfesionalesAreasRepository extends JpaRepository<VistaProfesionalesAreas, Long> {

    // Método para encontrar profesionales por ID de área
    Page<VistaProfesionalesAreas> findByIdArea(Long idArea, Pageable pageable);
}