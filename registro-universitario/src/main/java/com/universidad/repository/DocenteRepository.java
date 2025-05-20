package com.universidad.repository;

import com.universidad.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT d FROM Docente d WHERE LOWER(d.nombre) LIKE LOWER(concat('%', :nombre, '%')) " +
            "OR LOWER(d.apellido) LIKE LOWER(concat('%', :nombre, '%'))")
    List<Docente> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT d FROM Docente d WHERE d.usuario.id = :usuarioId")
    Optional<Docente> findByUsuarioId(Long usuarioId);

    @Query("SELECT d FROM Docente d WHERE d.especialidad = :especialidad")
    List<Docente> findByEspecialidad(String especialidad);
}