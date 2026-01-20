package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LibroInter {

    @Id
    @Column(name = "bookID")
    private Integer bookID;
    
    @Column(name = "Titulo", length = 300, nullable = false)
    private String titulo;

    @Column(name = "Autor", nullable = false)
    private String autor;

    @Column(name = "ISBN", length = 50, nullable = false)
    private String isbn;

    @Column(name = "Editorial", length = 50, nullable = false)
    private String editorial;

    private String fuente; //ESCOM, ESFM ETC.

    public LibroInter(){}

    public LibroInter(Integer bookID, String titulo, String autor, String isbn, String editorial, String fuente){
        this.bookID = bookID; 
        this.titulo =titulo; 
        this.autor = autor; 
        this.isbn = isbn; 
        this.editorial = editorial; 
        this.fuente = fuente; 
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAutor() {
        return autor;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getFuente() {
        return fuente;
    }
}
