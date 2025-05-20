package com.universidad.dto;

import com.universidad.model.Inscripcion;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionDTO {
    private Long id;

    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long estudianteId;

    private String estudianteNombre;

    @NotNull(message = "El ID de la materia es obligatorio")
    private Long materiaId;

    private String materiaNombre;

    private LocalDateTime fechaInscripcion;
    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaRechazo;
    private Inscripcion.Estado estado;
}