package com.universidad.repository;

import com.universidad.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByDni(String dni);
    Optional<Estudiante> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);

    @Query("SELECT e FROM Estudiante e WHERE LOWER(e.nombre) LIKE LOWER(concat('%', :nombre, '%'))")
    List<Estudiante> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT e FROM Estudiante e WHERE e.usuario.id = :usuarioId")
    Optional<Estudiante> findByUsuarioId(Long usuarioId);
}