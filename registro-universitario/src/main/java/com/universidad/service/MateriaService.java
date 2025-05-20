package com.universidad.service;

import com.universidad.dto.MateriaDTO;
import com.universidad.exception.ResourceNotFoundException;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;

    @Cacheable("materias")
    public List<MateriaDTO> findAll() {
        return materiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "materia", key = "#id")
    public MateriaDTO findById(Long id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con id: " + id));
        return convertToDTO(materia);
    }

    @Transactional
    @CacheEvict(value = {"materias", "materia"}, allEntries = true)
    public MateriaDTO save(MateriaDTO materiaDTO) {
        if (materiaRepository.existsByCodigo(materiaDTO.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una materia con el código: " + materiaDTO.getCodigo());
        }

        Materia materia = new Materia();
        materia.setNombre(materiaDTO.getNombre());
        materia.setCodigo(materiaDTO.getCodigo());
        materia.setCreditos(materiaDTO.getCreditos());
        materia.setDescripcion(materiaDTO.getDescripcion());

        if (materiaDTO.getDocenteId() != null) {
            materia.setDocente(docenteRepository.findById(materiaDTO.getDocenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con id: " + materiaDTO.getDocenteId())));
        }

        Materia savedMateria = materiaRepository.save(materia);
        return convertToDTO(savedMateria);
    }

    @Transactional
    @CacheEvict(value = {"materias", "materia"}, key = "#id")
    public MateriaDTO update(Long id, MateriaDTO materiaDTO) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con id: " + id));

        if (!materia.getCodigo().equals(materiaDTO.getCodigo()) &&
                materiaRepository.existsByCodigo(materiaDTO.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una materia con el código: " + materiaDTO.getCodigo());
        }

        materia.setNombre(materiaDTO.getNombre());
        materia.setCodigo(materiaDTO.getCodigo());
        materia.setCreditos(materiaDTO.getCreditos());
        materia.setDescripcion(materiaDTO.getDescripcion());

        if (materiaDTO.getDocenteId() != null) {
            materia.setDocente(docenteRepository.findById(materiaDTO.getDocenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con id: " + materiaDTO.getDocenteId())));
        } else {
            materia.setDocente(null);
        }

        Materia updatedMateria = materiaRepository.save(materia);
        return convertToDTO(updatedMateria);
    }

    @Transactional
    @CacheEvict(value = {"materias", "materia"}, key = "#id")
    public void delete(Long id) {
        if (!materiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Materia no encontrada con id: " + id);
        }
        materiaRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {"materias", "materia"}, key = "#materiaId")
    public MateriaDTO asignarDocente(Long materiaId, Long docenteId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con id: " + materiaId));

        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con id: " + docenteId));

        materia.setDocente(docente);
        Materia updatedMateria = materiaRepository.save(materia);
        return convertToDTO(updatedMateria);
    }

    private MateriaDTO convertToDTO(Materia materia) {
        MateriaDTO dto = new MateriaDTO();
        dto.setId(materia.getId());
        dto.setNombre(materia.getNombre());
        dto.setCodigo(materia.getCodigo());
        dto.setCreditos(materia.getCreditos());
        dto.setDescripcion(materia.getDescripcion());

        if (materia.getDocente() != null) {
            dto.setDocenteId(materia.getDocente().getId());
            dto.setDocenteNombre(materia.getDocente().getNombre() + " " + materia.getDocente().getApellido());
        }

        return dto;
    }
}