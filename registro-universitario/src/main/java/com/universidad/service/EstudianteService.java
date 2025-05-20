package com.universidad.service;

import com.universidad.dto.EstudianteDTO;
import com.universidad.exception.ResourceNotFoundException;
import com.universidad.model.Estudiante;
import com.universidad.model.Usuario;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    @Cacheable("estudiantes")
    public List<EstudianteDTO> findAll() {
        return estudianteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "estudiante", key = "#id")
    public EstudianteDTO findById(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
        return convertToDTO(estudiante);
    }

    @Transactional
    @CacheEvict(value = {"estudiantes", "estudiante"}, allEntries = true)
    public EstudianteDTO save(EstudianteDTO estudianteDTO) {
        if (estudianteRepository.existsByDni(estudianteDTO.getDni())) {
            throw new IllegalArgumentException("Ya existe un estudiante con el DNI: " + estudianteDTO.getDni());
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(estudianteDTO.getNombre());
        estudiante.setApellido(estudianteDTO.getApellido());
        estudiante.setDni(estudianteDTO.getDni());
        estudiante.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        estudiante.setEmail(estudianteDTO.getEmail());
        estudiante.setTelefono(estudianteDTO.getTelefono());

        Estudiante savedEstudiante = estudianteRepository.save(estudiante);
        return convertToDTO(savedEstudiante);
    }

    @Transactional
    public void crearEstudianteDesdeUsuario(Usuario usuario) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(usuario.getUsername());
        estudiante.setEmail(usuario.getEmail());
        estudiante.setUsuario(usuario);
        estudianteRepository.save(estudiante);
    }

    @Transactional
    @CacheEvict(value = {"estudiantes", "estudiante"}, key = "#id")
    public EstudianteDTO update(Long id, EstudianteDTO estudianteDTO) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        if (!estudiante.getDni().equals(estudianteDTO.getDni()) &&
                estudianteRepository.existsByDni(estudianteDTO.getDni())) {
            throw new IllegalArgumentException("Ya existe un estudiante con el DNI: " + estudianteDTO.getDni());
        }

        estudiante.setNombre(estudianteDTO.getNombre());
        estudiante.setApellido(estudianteDTO.getApellido());
        estudiante.setDni(estudianteDTO.getDni());
        estudiante.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        estudiante.setEmail(estudianteDTO.getEmail());
        estudiante.setTelefono(estudianteDTO.getTelefono());

        Estudiante updatedEstudiante = estudianteRepository.save(estudiante);
        return convertToDTO(updatedEstudiante);
    }

    @Transactional
    @CacheEvict(value = {"estudiantes", "estudiante"}, key = "#id")
    public void delete(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    private EstudianteDTO convertToDTO(Estudiante estudiante) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getId());
        dto.setNombre(estudiante.getNombre());
        dto.setApellido(estudiante.getApellido());
        dto.setDni(estudiante.getDni());
        dto.setFechaNacimiento(estudiante.getFechaNacimiento());
        dto.setEmail(estudiante.getEmail());
        dto.setTelefono(estudiante.getTelefono());

        if (estudiante.getUsuario() != null) {
            dto.setUsuarioId(estudiante.getUsuario().getId());
            dto.setUsername(estudiante.getUsuario().getUsername());
        }

        return dto;
    }
}