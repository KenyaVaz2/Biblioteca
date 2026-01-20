package com.library.model;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "LIBRO")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Libro")
    private Long idLibro;

    @Column(name = "ISBN", length = 50, nullable = false)
    private String isbn;

    @Column(name = "Idioma", length = 20, nullable = false)
    private String idioma;

    @Column(name = "Fecha_Publicacion", nullable = false)
    private Date fechaPublicacion;
    
    @ManyToOne
    @JoinColumn(name = "ID_Editorial", nullable = false)
    private Editorial editorial;

    @Column(name = "Titulo", length = 200, nullable = false)
    private String titulo;

    @Column(name = "Genero", length = 50, nullable = false)
    private String genero; 

    @ManyToMany
    @JoinTable(name = "AUTOR_LIBRO", joinColumns= @JoinColumn(name = "ID_Libro"), inverseJoinColumns = @JoinColumn(name = "ID_Autor"))
    @JsonIgnoreProperties("libros")
    private Set<Autor> autores = new HashSet<>();
    
    public Libro() {}

    public Libro(String isbn, String idioma, Date fechaPublicacion, Editorial editorial, String titulo, String genero, Set<Autor> autores ){
        this.isbn = isbn; 
        this.idioma = idioma; 
        this.fechaPublicacion = fechaPublicacion; 
        this.editorial = editorial; 
        this.titulo = titulo; 
        this.genero = genero; 
        this.autores = autores; 
    }

    public Long getId() {
        return idLibro;
    }
    public void setId(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIdioma() {
        return idioma;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Editorial getEditorial() {
        return editorial;
    }
    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Set<Autor> getAutores() {
        return autores;
    }
    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

}
