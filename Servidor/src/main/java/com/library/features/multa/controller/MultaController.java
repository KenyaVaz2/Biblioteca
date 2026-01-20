package com.library.features.multa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.features.multa.service.MultaService;
import com.library.model.Multa;

@RestController
@RequestMapping("/api/multas")
@CrossOrigin(origins = "http://localhost:5173")
public class MultaController {

    @Autowired
    private MultaService multaService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Multa>> getMultasPorUsuario(@PathVariable Long idUsuario) {
        List<Multa> multas = multaService.getMultasByUsuario(idUsuario);
        return ResponseEntity.ok(multas);
    }

    @GetMapping
    public ResponseEntity<List<Multa>> getAllMultas() {
        return ResponseEntity.ok(multaService.getAllMultas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Multa> getMultaById(@PathVariable Integer id) {

        Multa multa = multaService.getMultaById(id);

        if (multa == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(multa);
    }

    @PostMapping
    public ResponseEntity<Multa> createMulta(@RequestBody Multa multa) {

        Multa nuevaMulta = multaService.createMulta(multa);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMulta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Multa> updateMulta(
            @PathVariable Integer id,
            @RequestBody Multa multa) {

        Multa multaActualizada = multaService.updateMulta(id, multa);

        if (multaActualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(multaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMulta(@PathVariable Integer id) {

        boolean eliminado = multaService.deleteMulta(id);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
