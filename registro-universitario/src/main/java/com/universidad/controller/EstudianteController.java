package com.universidad.controller;

import com.universidad.dto.EstudianteDTO;
import com.universidad.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    @Operation(summary = "Obtener todos los estudiantes", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')")
    public ResponseEntity<List<EstudianteDTO>> getAll() {
        return ResponseEntity.ok(estudianteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<EstudianteDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo estudiante", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> create(@Valid @RequestBody EstudianteDTO estudianteDTO) {
        return ResponseEntity.ok(estudianteService.save(estudianteDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estudiante", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<EstudianteDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        return ResponseEntity.ok(estudianteService.update(id, estudianteDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estudiante", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}