package com.universidad.repository;

import com.universidad.model.Inscripcion;
import com.universidad.model.Inscripcion.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);

    @Query("SELECT i FROM Inscripcion i WHERE i.materia.id = :materiaId")
    List<Inscripcion> findByMateriaId(@Param("materiaId") Long materiaId);

    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.materia.id = :materiaId AND i.estado = :estado")
    Long countByMateriaIdAndEstado(@Param("materiaId") Long materiaId, @Param("estado") Estado estado);

    @Query("SELECT i FROM Inscripcion i WHERE i.estado = 'PENDIENTE'")
    List<Inscripcion> findInscripcionesPendientes();

    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.materia.id = :materiaId")
    Optional<Inscripcion> findByEstudianteAndMateria(
            @Param("estudianteId") Long estudianteId,
            @Param("materiaId") Long materiaId);
}