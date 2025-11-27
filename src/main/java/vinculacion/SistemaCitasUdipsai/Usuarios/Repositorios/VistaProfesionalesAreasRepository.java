package vinculacion.SistemaCitasUdipsai.Usuarios.Repositorios;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vinculacion.SistemaCitasUdipsai.Usuarios.views.VistaProfesionalesAreas;

public interface VistaProfesionalesAreasRepository extends JpaRepository<VistaProfesionalesAreas, Long> {

    // Método para encontrar profesionales por ID de área
    Page<VistaProfesionalesAreas> findByIdArea(Long idArea, Pageable pageable);
}