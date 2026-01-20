package com.library.features.editorial.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.features.editorial.service.EditorialService;
import com.library.model.Editorial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/editoriales")
@CrossOrigin(origins = "http://localhost:5173")
public class EditorialController {

    private final EditorialService service; 

    public EditorialController(EditorialService service){ 
        this.service = service;
    }

    @GetMapping
    public List<Editorial> findAll() {
        return service.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Editorial> findById(@PathVariable Long id) {
        Editorial editorial = service.findById(id);
        
        if(editorial == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(editorial);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> update(@PathVariable Long id, @RequestBody Editorial editorial) {
        
        editorial.setId(id);
        Editorial actualizar = service.update(editorial);
        
        if(actualizar == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizar);
    }


    @PostMapping
    public ResponseEntity<Editorial> save(@RequestBody Editorial editorial) {
        Editorial nuevoE = service.save(editorial);
        
        System.out.println("Editorial recibido: "+ editorial);
    
        return ResponseEntity.status(201).body(nuevoE); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditorial(@PathVariable Long id){
        boolean eliminado = service.deleteEditorial(id);

        if(!eliminado){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build(); 
    }    
}
