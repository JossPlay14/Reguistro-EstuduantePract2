package com.universidad.service;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;
import com.universidad.model.Usuario;
import com.universidad.repository.DocenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository docenteRepository;

    @Transactional
    public void crearDocenteDesdeUsuario(Usuario usuario) {
        Docente docente = new Docente();
        docente.setNombre(usuario.getUsername()); // Usamos el username como nombre inicial
        docente.setApellido("Apellido"); // Valor por defecto
        docente.setEmail(usuario.getEmail());
        docente.setEspecialidad("Por definir");
        docente.setUsuario(usuario);
        docenteRepository.save(docente);
    }

    // Resto de métodos permanecen igual...
    @Cacheable("docentes")
    public List<DocenteDTO> findAll() {
        return docenteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "docente", key = "#id")
    public DocenteDTO findById(Long id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        return convertToDTO(docente);
    }

    @Transactional
    @CacheEvict(value = {"docentes", "docente"}, allEntries = true)
    public DocenteDTO save(DocenteDTO docenteDTO) {
        Docente docente = new Docente();
        docente.setNombre(docenteDTO.getNombre());
        docente.setApellido(docenteDTO.getApellido());
        docente.setEmail(docenteDTO.getEmail());
        docente.setEspecialidad(docenteDTO.getEspecialidad());

        Docente savedDocente = docenteRepository.save(docente);
        return convertToDTO(savedDocente);
    }

    @Transactional
    @CacheEvict(value = {"docentes", "docente"}, key = "#id")
    public DocenteDTO update(Long id, DocenteDTO docenteDTO) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        docente.setNombre(docenteDTO.getNombre());
        docente.setApellido(docenteDTO.getApellido());
        docente.setEmail(docenteDTO.getEmail());
        docente.setEspecialidad(docenteDTO.getEspecialidad());

        Docente updatedDocente = docenteRepository.save(docente);
        return convertToDTO(updatedDocente);
    }

    @Transactional
    @CacheEvict(value = {"docentes", "docente"}, key = "#id")
    public void delete(Long id) {
        docenteRepository.deleteById(id);
    }

    private DocenteDTO convertToDTO(Docente docente) {
        DocenteDTO dto = new DocenteDTO();
        dto.setId(docente.getId());
        dto.setNombre(docente.getNombre());
        dto.setApellido(docente.getApellido());
        dto.setEmail(docente.getEmail());
        dto.setEspecialidad(docente.getEspecialidad());

        if (docente.getUsuario() != null) {
            dto.setUsuarioId(docente.getUsuario().getId());
            dto.setUsername(docente.getUsuario().getUsername());
        }

        return dto;
    }
}