package com.ejemplo.usuarios.controller;

import com.ejemplo.usuarios.dto.ApiResponse;
import com.ejemplo.usuarios.dto.LoginRequest;
import com.ejemplo.usuarios.dto.RegisterRequest;
import com.ejemplo.usuarios.dto.UsuarioResponse;
import com.ejemplo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrar(@Valid @RequestBody RegisterRequest request) {
        UsuarioResponse nuevoUsuario = usuarioService.registrar(request);
        ApiResponse<UsuarioResponse> respuesta = ApiResponse.exito("Usuario registrado exitosamente", nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioResponse>> login(@Valid @RequestBody LoginRequest request) {
        UsuarioResponse usuarioAutenticado = usuarioService.login(request);
        ApiResponse<UsuarioResponse> respuesta = ApiResponse.exito("Inicio de sesión exitoso", usuarioAutenticado);
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
}
