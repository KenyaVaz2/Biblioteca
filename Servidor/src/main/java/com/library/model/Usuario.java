package com.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Usuario")
    private Long idUsuario; 

    @Column(name = "Nombre", nullable = false)
    private String nombre; 

    @Column(name = "Contraseña", nullable = false)
    private String password;

    @Column(name = "Correo", nullable = false)
    private String correo;

    @Column(name = "Rol", nullable = false)
    private String rol;

    public Usuario() {}


    public Usuario(String nombre, String password, String correo, String rol) {
        this.nombre = nombre;
        this.password = password;
        this.correo = correo;
        this.rol = rol;
    }


    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
