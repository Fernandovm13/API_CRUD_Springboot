package com.ejemplo.usuarios.service;

import com.ejemplo.usuarios.dto.LoginRequest;
import com.ejemplo.usuarios.dto.RegisterRequest;
import com.ejemplo.usuarios.dto.UpdateUsuarioRequest;
import com.ejemplo.usuarios.dto.UsuarioResponse;
import com.ejemplo.usuarios.entity.Usuario;
import com.ejemplo.usuarios.exception.BadRequestException;
import com.ejemplo.usuarios.exception.RecursoNoEncontradoException;
import com.ejemplo.usuarios.exception.UnauthorizedException;
import com.ejemplo.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email '" + request.getEmail() + "' ya se encuentra registrado.");
        }

        String passwordEncriptada = passwordEncoder.encode(request.getPassword());

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                passwordEncriptada,
                "activo"
        );

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return UsuarioResponse.fromEntity(usuarioGuardado);
    }

    public UsuarioResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas (email o contraseña incorrectos)."));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas (email o contraseña incorrectos).");
        }

        return UsuarioResponse.fromEntity(usuario);
    }

    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public UsuarioResponse obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + id + " no fue encontrado."));

        return UsuarioResponse.fromEntity(usuario);
    }

    public UsuarioResponse actualizar(Long id, UpdateUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + id + " no fue encontrado."));

        if (!usuario.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email '" + request.getEmail() + "' ya está registrado por otro usuario.");
        }

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            usuario.setEstado(request.getEstado());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return UsuarioResponse.fromEntity(usuarioActualizado);
    }

    public void eliminarODesactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + id + " no fue encontrado."));

        usuario.setEstado("inactivo");
        usuarioRepository.save(usuario);
    }
}
