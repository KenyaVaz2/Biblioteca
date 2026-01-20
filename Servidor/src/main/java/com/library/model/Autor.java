package com.library.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "AUTOR")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Autor", nullable = false)
    private Long idAutor; 

    @Column(name = "Nombre", nullable = false, length=50)
    private String nombre; 

    @Column(name = "Apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "Nacionalidad", length = 50, nullable = true)
    private String nacionalidad;
    
    @Column(name = "Fecha_Nac", nullable = true)
    private Date fecha_nac;

    @ManyToMany(mappedBy = "autores")
    @JsonIgnoreProperties("autores")
    private Set<Libro> libros = new HashSet<>();
    
    public Autor() {}

    public Autor(String nombre, String apellido, String nacionalidad, Date fecha_nac, Set<Libro> libros){
        this.nombre = nombre; 
        this.apellido = apellido; 
        this.nacionalidad = nacionalidad; 
        this.fecha_nac = fecha_nac;
        this.libros = libros; 
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

        public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

}

