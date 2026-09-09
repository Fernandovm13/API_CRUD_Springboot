package com.ejemplo.usuarios.dto;

import com.ejemplo.usuarios.entity.Usuario;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private String estado;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String nombre, String email, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estado = estado;
    }

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getEstado()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
