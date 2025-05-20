package com.universidad.service;

import com.universidad.dto.AuthResponse;
import com.universidad.dto.LoginRequest;
import com.universidad.dto.RegisterRequest;
import com.universidad.model.Rol;
import com.universidad.model.Usuario;
import com.universidad.repository.UsuarioRepository;
import com.universidad.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EstudianteService estudianteService;
    private final DocenteService docenteService;

    public AuthResponse register(RegisterRequest request) {
        // Validar que el rol sea válido
        Rol rol;
        try {
            rol = Rol.valueOf(request.getRol());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + request.getRol());
        }

        // Verificar si el username ya existe
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear usuario
        var usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .rol(rol)
                .build();

        usuarioRepository.save(usuario);

        // Crear perfil según el rol
        if (rol == Rol.ESTUDIANTE) {
            estudianteService.crearEstudianteDesdeUsuario(usuario);
        } else if (rol == Rol.DOCENTE) {
            docenteService.crearDocenteDesdeUsuario(usuario);
        }

        var jwtToken = jwtService.generateToken(usuario);
        return AuthResponse.builder()
                .token(jwtToken)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        var jwtToken = jwtService.generateToken(usuario);
        return AuthResponse.builder()
                .token(jwtToken)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}