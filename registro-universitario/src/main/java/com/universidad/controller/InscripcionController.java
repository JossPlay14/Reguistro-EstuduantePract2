package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion;
import com.universidad.service.InscripcionService;
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
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @GetMapping
    @Operation(summary = "Obtener todas las inscripciones", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')")
    public ResponseEntity<List<InscripcionDTO>> getAll() {
        return ResponseEntity.ok(inscripcionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<InscripcionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.findById(id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Obtener inscripciones por estudiante", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<List<InscripcionDTO>> getByEstudianteId(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(inscripcionService.findByEstudianteId(estudianteId));
    }

    @PostMapping
    @Operation(summary = "Crear nueva inscripción", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<InscripcionDTO> create(@Valid @RequestBody InscripcionDTO inscripcionDTO) {
        return ResponseEntity.ok(inscripcionService.save(inscripcionDTO));
    }

    // Endpoint existente (puedes mantenerlo o reemplazarlo)
    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de inscripción", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')")
    @CacheEvict(value = {"inscripciones", "inscripcion", "inscripcionesByEstudiante", "inscripcionesByMateria"}, key = "#id")
    public ResponseEntity<InscripcionDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam Inscripcion.Estado estado) {
        return ResponseEntity.ok(inscripcionService.updateEstado(id, estado));
    }

    // Nuevo endpoint con la estructura que solicitas
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de inscripción", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')")
    @CacheEvict(value = {"inscripciones", "inscripcion", "inscripcionesByEstudiante", "inscripcionesByMateria"}, key = "#id")
    public ResponseEntity<InscripcionDTO> cambiarEstadoInscripcion(
            @PathVariable Long id,
            @RequestParam Inscripcion.Estado estado) {
        return ResponseEntity.ok(inscripcionService.updateEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inscripción", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscripcionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}