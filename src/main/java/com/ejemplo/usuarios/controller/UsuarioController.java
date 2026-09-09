package com.ejemplo.usuarios.controller;

import com.ejemplo.usuarios.dto.ApiResponse;
import com.ejemplo.usuarios.dto.UpdateUsuarioRequest;
import com.ejemplo.usuarios.dto.UsuarioResponse;
import com.ejemplo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> obtenerTodos() {
        List<UsuarioResponse> usuarios = usuarioService.obtenerTodos();
        ApiResponse<List<UsuarioResponse>> respuesta = ApiResponse.exito("Lista de usuarios obtenida", usuarios);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.obtenerPorId(id);
        ApiResponse<UsuarioResponse> respuesta = ApiResponse.exito("Usuario encontrado", usuario);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizar(@PathVariable Long id, @Valid @RequestBody UpdateUsuarioRequest request) {
        UsuarioResponse usuarioActualizado = usuarioService.actualizar(id, request);
        ApiResponse<UsuarioResponse> respuesta = ApiResponse.exito("Usuario actualizado correctamente", usuarioActualizado);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        usuarioService.eliminarODesactivar(id);
        ApiResponse<String> respuesta = ApiResponse.exito("Usuario desactivado/eliminado correctamente", "ID: " + id);
        return ResponseEntity.ok(respuesta);
    }
}
