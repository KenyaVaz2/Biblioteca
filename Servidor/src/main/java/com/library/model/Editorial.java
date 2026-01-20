package com.library.model;
import jakarta.persistence.*;

@Entity
@Table(name = "EDITORIAL")
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "Direccion", nullable = true, length = 300)
    private String direccion;

    @Column(name = "Telefono", nullable = true, length = 10)
    private String telefono;

    @Column(name = "SitioWeb", nullable = true, length = 150)
    private String sitioWeb;

    public Editorial() {}

    public Editorial(Long id, String nombre, String direccion, String telefono, String sitioWeb) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.sitioWeb = sitioWeb;
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

    public String getDireccion() {
        return direccion; 
    }
    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }

    public String getTelefono() { 
        return telefono; 
    
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono; 
    }

    public String getSitioWeb() { 
        return sitioWeb; 
    
    }
    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb; 
    }
}
