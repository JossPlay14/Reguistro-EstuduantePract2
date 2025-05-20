package com.universidad.controller;

import com.universidad.dto.AuthResponse;
import com.universidad.dto.DocenteDTO;
import com.universidad.dto.LoginRequest;
import com.universidad.dto.RegisterRequest;
import com.universidad.service.AuthService;
import com.universidad.service.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    private final AuthService authService;
    private final DocenteService docenteService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/register/docente")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar nuevo docente")
    public ResponseEntity<AuthResponse> registerDocente(@RequestBody @Valid DocenteDTO docenteDTO) {
        // Crear RegisterRequest a partir de DocenteDTO
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(generarUsername(docenteDTO.getNombre(), docenteDTO.getApellido()));
        registerRequest.setPassword("TempPassword123"); // Contraseña temporal
        registerRequest.setEmail(docenteDTO.getEmail());
        registerRequest.setRol("DOCENTE");

        // Registrar el usuario docente (esto creará automáticamente el perfil docente)
        AuthResponse response = authService.register(registerRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    private String generarUsername(String nombre, String apellido) {
        return (nombre.charAt(0) + apellido.split(" ")[0]).toLowerCase();
    }
}