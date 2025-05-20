package com.universidad.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @NotNull(message = "La materia es obligatoria")
    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @NotNull(message = "La fecha de inscripción es obligatoria")
    private LocalDateTime fechaInscripcion;

    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaRechazo;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado {
        PENDIENTE,
        APROBADA,
        RECHAZADA,
        CANCELADA
    }
}