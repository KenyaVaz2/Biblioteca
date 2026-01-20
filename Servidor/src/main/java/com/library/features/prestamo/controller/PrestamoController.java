package com.library.features.prestamo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.features.multa.service.MultaService;
import com.library.features.prestamo.service.PrestamoService;
import com.library.model.Prestamo;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "http://localhost:5173")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final MultaService multaService;

    public PrestamoController(PrestamoService prestamoService,MultaService multaService) {
        this.prestamoService = prestamoService;
        this.multaService = multaService; 
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Void> pagarMulta(@PathVariable Integer id) {
        multaService.pagarMulta(id);
        return ResponseEntity.ok().build();
    }

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Prestamo>> getAllPrestamos() {
        return ResponseEntity.ok(prestamoService.getAllPrestamos());
    }

    // LISTAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> getPrestamoById(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.getPrestamoById(id);

        if (prestamo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(prestamo);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> createPrestamo(@RequestBody Prestamo prestamo) {
        try {
            Prestamo nuevo = prestamoService.createPrestamo(prestamo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            // Si falla la regla de los 5 libros, devolvemos error 400 con el mensaje
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //ver mis prestamos 
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Prestamo>> getPrestamosByUsuario(@PathVariable Long idUsuario) {
        List<Prestamo> lista = prestamoService.getPrestamosByUsuario(idUsuario);
        return ResponseEntity.ok(lista);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> updatePrestamo(
            @PathVariable Long id,
            @RequestBody Prestamo prestamo) {

        Prestamo actualizado = prestamoService.updatePrestamo(id, prestamo);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Long id) {

        boolean eliminado = prestamoService.deletePrestamo(id);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
