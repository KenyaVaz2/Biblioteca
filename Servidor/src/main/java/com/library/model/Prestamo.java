package com.library.model;

import java.sql.Date;


public class Prestamo {

    private Long idPrestamo;
    private Date fechaInicio;
    private Date fechaDevolucion;
    private Long idUsuario;
    private Long idEjemplar;
    private String estado;
    private Usuario usuario; 
    private Ejemplar ejemplar;

    public Prestamo() {}

    public Prestamo(Date fechaInicio, Date fechaDevolucion, Long idUsuario, Long idEjemplar, String estado, Usuario usuario, Ejemplar ejemplar) {
        this.fechaInicio = fechaInicio;
        this.fechaDevolucion = fechaDevolucion;
        this.idUsuario = idUsuario;
        this.idEjemplar = idEjemplar;
        this.estado = estado; 
        this.usuario = usuario; 
        this.ejemplar = ejemplar; 
    }

    public Long getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Long idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(Long idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }
}
