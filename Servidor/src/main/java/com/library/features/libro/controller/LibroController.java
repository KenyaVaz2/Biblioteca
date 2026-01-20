package com.library.features.libro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.features.libro.service.LibroService;
import com.library.model.Libro;


@RestController
@RequestMapping("api/libros")
@CrossOrigin(origins = "http://localhost:5173")
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    // titulo
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Libro>> buscarLibroTitulo(@RequestParam String titulo) {
        List<Libro> resultados = service.getLibroByTitulo(titulo);
        return ResponseEntity.ok(resultados);
    }

    //isbn
    @GetMapping("/buscar/isbn")
    public ResponseEntity<Libro> buscarLibroIsbn(@RequestParam String isbn) {
        Libro libro = service.getLibroByIsbn(isbn);
        if (libro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libro);
    }

    //genero 
    @GetMapping("/buscar/genero")
    public ResponseEntity<List<Libro>> buscarLibroGenero(@RequestParam String genero){
        List<Libro> resultados = service.getLibroByGenero(genero);
        return ResponseEntity.ok(resultados);
    }

    //autor
    @GetMapping("/buscar/autor")
    public ResponseEntity<List<Libro>> buscarLibroAutor(@RequestParam String nombreAutor){
        List<Libro> resultados = service.getLibroByAutor(nombreAutor);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscar/general")
    public ResponseEntity<List<Libro>> buscarGeneral(@RequestParam String query) {
        List<Libro> resultados = service.buscarEnTodoLocal(query);
        return ResponseEntity.ok(resultados);
    }

    //busqueda interbibliotecaria
    @GetMapping("buscar/interbibliotecario")
    public ResponseEntity<?> buscarInterbibliotecario(@RequestParam String titulo) {
        List<?> resultados = service.busquedaGeneral(titulo);

        if(resultados.isEmpty()){
            return ResponseEntity.status(404).body("No se encontraron coincidencias en ninguna biblioteca");
        }
        return ResponseEntity.ok(resultados);
    }
    

    // LISTAR TODOS 
    @GetMapping
    public List<Libro> getAllLibros() {
        return service.getAllLibros();
    }

    // LISTAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibroById(@PathVariable Long id) {
        Libro libro = service.getLibroById(id);
        if (libro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libro);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<Libro> createLibro(@RequestBody Libro libro) {
        Libro nuevo = service.createLibro(libro);
        return ResponseEntity.status(201).body(nuevo);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable Long id, @RequestBody Libro libro) {
        Libro actualizado = service.updateLibro(id, libro);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        boolean eliminado = service.deleteLibro(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}



