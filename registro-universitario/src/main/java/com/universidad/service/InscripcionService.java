package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import com.universidad.exception.ResourceNotFoundException;
import com.universidad.model.*;
import com.universidad.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;

    @Cacheable("inscripciones")
    public List<InscripcionDTO> findAll() {
        return inscripcionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "inscripcion", key = "#id")
    public InscripcionDTO findById(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada con id: " + id));
        return convertToDTO(inscripcion);
    }

    @Cacheable(value = "inscripcionesByEstudiante", key = "#estudianteId")
    public List<InscripcionDTO> findByEstudianteId(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "inscripcionesByMateria", key = "#materiaId")
    public List<InscripcionDTO> findByMateriaId(Long materiaId) {
        return inscripcionRepository.findByMateriaId(materiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"inscripciones", "inscripcion", "inscripcionesByEstudiante", "inscripcionesByMateria"}, allEntries = true)
    public InscripcionDTO save(InscripcionDTO inscripcionDTO) {
        if (inscripcionRepository.existsByEstudianteIdAndMateriaId(
                inscripcionDTO.getEstudianteId(), inscripcionDTO.getMateriaId())) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esta materia");
        }

        Estudiante estudiante = estudianteRepository.findById(inscripcionDTO.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + inscripcionDTO.getEstudianteId()));

        Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con id: " + inscripcionDTO.getMateriaId()));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado(Inscripcion.Estado.PENDIENTE);

        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(savedInscripcion);
    }

    @Transactional
    @CacheEvict(value = {"inscripciones", "inscripcion", "inscripcionesByEstudiante", "inscripcionesByMateria"}, key = "#id")
    public InscripcionDTO updateEstado(Long id, Inscripcion.Estado estado) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada con id: " + id));

        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        if (inscripcion.getEstado() == Inscripcion.Estado.APROBADA && estado != Inscripcion.Estado.APROBADA) {
            throw new IllegalStateException("No se puede modificar el estado de una inscripción ya aprobada");
        }

        // Lógica para actualizar fechas según el estado
        if (estado == Inscripcion.Estado.APROBADA) {
            inscripcion.setFechaAprobacion(LocalDateTime.now());
            inscripcion.setFechaRechazo(null);
        } else if (estado == Inscripcion.Estado.RECHAZADA) {
            inscripcion.setFechaRechazo(LocalDateTime.now());
            inscripcion.setFechaAprobacion(null);
        } else {
            // Para otros estados (PENDIENTE, CANCELADA)
            inscripcion.setFechaAprobacion(null);
            inscripcion.setFechaRechazo(null);
        }

        inscripcion.setEstado(estado);
        Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(updatedInscripcion);
    }

    @Transactional
    @CacheEvict(value = {"inscripciones", "inscripcion", "inscripcionesByEstudiante", "inscripcionesByMateria"}, key = "#id")
    public void delete(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscripción no encontrada con id: " + id);
        }
        inscripcionRepository.deleteById(id);
    }

    @Transactional
    public InscripcionDTO inscribirEstudiante(String username, Long materiaId) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        InscripcionDTO inscripcionDTO = new InscripcionDTO();
        inscripcionDTO.setEstudianteId(estudiante.getId());
        inscripcionDTO.setMateriaId(materiaId);

        return save(inscripcionDTO);
    }

    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        InscripcionDTO dto = new InscripcionDTO();
        dto.setId(inscripcion.getId());
        dto.setEstudianteId(inscripcion.getEstudiante().getId());
        dto.setEstudianteNombre(inscripcion.getEstudiante().getNombre() + " " + inscripcion.getEstudiante().getApellido());
        dto.setMateriaId(inscripcion.getMateria().getId());
        dto.setMateriaNombre(inscripcion.getMateria().getNombre());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        dto.setEstado(inscripcion.getEstado());
        dto.setFechaAprobacion(inscripcion.getFechaAprobacion());
        dto.setFechaRechazo(inscripcion.getFechaRechazo());
        return dto;
    }
}