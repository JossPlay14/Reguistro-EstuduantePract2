package com.universidad.controller;

import com.universidad.dto.MateriaDTO;
import com.universidad.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "materias")
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping
    @Operation(summary = "Obtener todas las materias", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<List<MateriaDTO>> getAll() {
        return ResponseEntity.ok(materiaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener materia por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<MateriaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materiaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva materia", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MateriaDTO> create(@Valid @RequestBody MateriaDTO materiaDTO) {
        return ResponseEntity.ok(materiaService.save(materiaDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar materia", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MateriaDTO> update(@PathVariable Long id, @Valid @RequestBody MateriaDTO materiaDTO) {
        return ResponseEntity.ok(materiaService.update(id, materiaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar materia", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materiaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idMateria}/asignar-docente/{idDocente}")
    @Operation(summary = "Asignar docente a materia", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"materias", "materia"}, key = "#idMateria")
    public ResponseEntity<MateriaDTO> asignarDocenteAMateria(
            @PathVariable Long idMateria,
            @PathVariable Long idDocente) {
        return ResponseEntity.ok(materiaService.asignarDocente(idMateria, idDocente));
    }
}