package com.library.features.libro.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.LibroInter.repository.LibroInterRepository;
import com.library.features.autor.service.AutorService;
import com.library.features.editorial.service.EditorialService;
import com.library.features.libro.repository.LibroRepository;
import com.library.model.Autor;
import com.library.model.Libro;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    private final AutorService autorService;
    private final EditorialService editorialService; 
    private final LibroInterRepository libroInterRepository;

    public LibroService(LibroRepository libroRepository, AutorService autorService, EditorialService editorialService, LibroInterRepository libroInterRepository){
        this.libroRepository = libroRepository;
        this.autorService = autorService; 
        this.editorialService = editorialService;
        this.libroInterRepository = libroInterRepository; 
    }

    // OBTENER POR ID
    public Libro getLibroById(Long id) {
        return libroRepository.findById(id);
    }

    //buscar por titulo
    public List<Libro> getLibroByTitulo(String titulo){
        return libroRepository.findByTitulo(titulo);
    }

    //buscar por ISBN
    public Libro getLibroByIsbn(String isbn){
        return libroRepository.findByIsbn(isbn);
    }

    //buscar por genero
    public List<Libro> getLibroByGenero(String genero){
        return libroRepository.findByGenero(genero);
    }

    //buscar por autor
    public List<Libro> getLibroByAutor(String nombreAutor){
        return libroRepository.findByAutorNombre(nombreAutor);
    }

    // LISTAR TODOS
    public List<Libro> getAllLibros() {
        return libroRepository.findAll();
    }

    // CREAR
    public Libro createLibro(Libro libro) {

    if (libro.getEditorial() == null || libro.getEditorial().getId() == null) {
        throw new RuntimeException("Error: El libro debe tener una editorial asignada.");
    }
    
    var editorialExiste = editorialService.findById(libro.getEditorial().getId());
    if (editorialExiste == null) {
        throw new RuntimeException("Error: La editorial con ID " + libro.getEditorial().getId() + " no existe.");
    }

    if (libro.getAutores() != null) {
        for (Autor autor : libro.getAutores()) {
            if (autorService.getAutorById(autor.getIdAutor()) == null) {
                throw new RuntimeException("Error: El autor con ID " + autor.getIdAutor() + " no existe.");
            }
        }
    }
    return libroRepository.save(libro);
    }

    // ACTUALIZAR
    public Libro updateLibro(Long id, Libro libro) {
        libro.setId(id);
        return libroRepository.update(libro);
    }

    // ELIMINAR
    public boolean deleteLibro(Long id) {
        return libroRepository.delete(id);
    }

    //* BUSQUEDA INTERBIBLIOTECARIA */
    public List<?> busquedaGeneral(String titulo){
        List<Libro> locales  = libroRepository.findByTitulo(titulo);

        if(!locales.isEmpty()){
            return locales; 
        }
        return libroInterRepository.buscarEnTodas(titulo);
    }

    public List<Libro> buscarEnTodoLocal(String query) {
        Set<Libro> resultados = new HashSet<>();

        // Buscamos por título
        resultados.addAll(libroRepository.findByTitulo(query));
    
        // Buscamos por ISBN
        Libro porIsbn = libroRepository.findByIsbn(query);
        if (porIsbn != null) resultados.add(porIsbn);
    
        // Buscamos por Género
        resultados.addAll(libroRepository.findByGenero(query));
    
        // Buscamos por Autor (Nombre o Apellido)
        resultados.addAll(libroRepository.findByAutorNombre(query));

        return new ArrayList<>(resultados);
}
}

