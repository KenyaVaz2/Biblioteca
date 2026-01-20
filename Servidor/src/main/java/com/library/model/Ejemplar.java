package com.library.model;

import java.sql.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "EJEMPLAR")
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Ejemplar")
    private long idEjemplar; 

    @ManyToOne
    @JoinColumn(name = "ID_Libro", nullable = false)  //LLAVE FORANEA
    private Libro libro; 

    @Column(name = "Fecha_Compra", nullable = false)
    private Date fecha_compra; 

    @Column(name = "Ubicacion", length = 50, nullable = false)
    private String ubicacion;

    @Column(name = "Estado", length = 50, nullable = false)
    private String estado;
/* 
    @Column(name="Titulo", length = 200, nullable = false)
    private String titulo;
*/
    @Column(nullable = false)
    private int cantidad;

    public Ejemplar() {}

    public Ejemplar(String titulo, int cantidad, Date fecha_compra, String ubicacion, String estado, Libro libro){
        this.fecha_compra = fecha_compra; 
        this.ubicacion = ubicacion; 
        this.libro = libro; 
        this.cantidad = cantidad; 
        this.estado = estado; //disponible 
    }

    public void setID_Ejemplar(long idE) {
        this.idEjemplar = idE; 
    }

    public Long getID_Ejemplar() {
        return idEjemplar;
    }

    public void setFecha_compra(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public Date getFecha_compra() {
        return fecha_compra;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado(){
        return estado; 
    }

    public void setLibro(Libro libro) { //LLAVE FORANEA
        this.libro = libro;
    }

    public Libro getLibro() { //LLAVE FORANEA
        return libro;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }
}
