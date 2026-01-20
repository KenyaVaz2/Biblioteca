package com.library.features.autor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.features.autor.service.AutorService;
import com.library.model.Autor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("api/autores") //back 
@CrossOrigin(origins = "http://localhost:5173") //front 
public class AutorController {

        private final AutorService service;

        public AutorController(AutorService service){
                this.service = service; 
        }
        
        @GetMapping
        public List<Autor> getAllAutores(){
                return service.getAllAutores();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Autor> getAutorById(@PathVariable Long id) {
                Autor autor = service.getAutorById(id); 

                if(autor == null){
                        return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(autor); 
        }

        //Actualizar autor 
        @PutMapping("/{id}")
        public ResponseEntity<Autor> updateAutor(@PathVariable Long id, @RequestBody Autor autor) {
                
                Autor actualizado = service.updateAutor(id, autor);

                if (actualizado == null) {
                        return ResponseEntity.notFound().build();
                }
        return ResponseEntity.ok(actualizado); 
        }

        // Crear autor
        @PostMapping
        public ResponseEntity<Autor> createAutor(@RequestBody Autor autor) {
                Autor nuevo = service.createAutor(autor);

                System.out.println("AUTOR RECIBIDO: " + autor);
                return ResponseEntity.status(201).body(nuevo);
        }
        
        //Eliminar Autor 
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteAutor(@PathVariable Long id){
                service.deleteAutor(id);
                return ResponseEntity.noContent().build(); 
        }
}
