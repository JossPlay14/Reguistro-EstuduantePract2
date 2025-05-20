package com.universidad.repository;

import com.universidad.model.Materia;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Optional<Materia> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    @Query("SELECT m FROM Materia m WHERE LOWER(m.nombre) LIKE LOWER(concat('%', :nombre, '%'))")
    List<Materia> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT m FROM Materia m WHERE m.docente.id = :docenteId")
    List<Materia> findByDocenteId(Long docenteId);

    @Query("SELECT m FROM Materia m WHERE m.docente IS NULL")
    List<Materia> findMateriasSinDocente();
}