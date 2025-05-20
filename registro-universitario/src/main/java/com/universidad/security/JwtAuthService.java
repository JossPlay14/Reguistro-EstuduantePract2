package com.universidad.security;

import com.universidad.dto.AuthResponse;
import com.universidad.dto.LoginRequest;
import com.universidad.dto.RegisterRequest;
import com.universidad.model.Rol;
import com.universidad.model.Usuario;
import com.universidad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService {  // Cambiado el nombre

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .rol(Rol.ESTUDIANTE)
                .build();

        usuarioRepository.save(usuario);

        var jwtToken = jwtService.generateToken(usuario);
        return AuthResponse.builder()
                .token(jwtToken)
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
                .orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}