package com.library.features.ejemplar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.features.ejemplar.service.EjemplarService;
import com.library.model.Ejemplar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/ejemplares")
@CrossOrigin(origins = "http://localhost:5173")
public class EjemplarController {

    private final EjemplarService service; 

    public EjemplarController(EjemplarService service){
        this.service = service; 
    }

    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<List<Ejemplar>> getEjemplaresPorLibro(@PathVariable Long idLibro) {
        List<Ejemplar> ejemplares = service.getEjemplaresPorLibro(idLibro);
        
        return ResponseEntity.ok(ejemplares); 
    }
    
    //Listar a todos 
    @GetMapping
    public List<Ejemplar> getEncontrartodos() {
        return service.getEncontrartodos();
    }

    //Listar por id
    @GetMapping("/{id}")
    public ResponseEntity<Ejemplar> getporID(@PathVariable Long id) {
        Ejemplar ejemplar = service.getporID(id);
        
        if(ejemplar == null){
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok(ejemplar); 
    }

    //Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> updateEjemplar(@PathVariable Long id, @RequestBody Ejemplar ejemplar) {
        
        ejemplar.setID_Ejemplar(id);
        Ejemplar actualizado = service.updatEjemplar(ejemplar); 

        if(actualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    //crear 
    @PostMapping
    public ResponseEntity<Ejemplar> crearEjemplar(@RequestBody Ejemplar ejemplar) {
        Ejemplar nuevo = service.crearEjemplar(ejemplar);

        System.out.println("EJEMPLAR RECIBIDO: "+ ejemplar);
        return ResponseEntity.status(201).body(nuevo); 
    }

    //eliminar 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplar(@PathVariable Long id){
        service.eliminarEjemplar(id);

        return ResponseEntity.noContent().build();
    }
}
