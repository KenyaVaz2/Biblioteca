package com.library.features.prestamo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.prestamo.repository.PrestamoRepo;
import com.library.model.Prestamo;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepo prestamoRepository;

    // BUSCAR POR ID
    public Prestamo getPrestamoById(Long id) {
        return prestamoRepository.findById(id);
    }

    // LISTAR TODOS
    public List<Prestamo> getAllPrestamos() {
        return prestamoRepository.findAll();
    }

    // CREAR
    public Prestamo createPrestamo(Prestamo prestamo) {

        boolean disponible = prestamoRepository.esEjemplarDisponible(prestamo.getIdEjemplar());
        if (!disponible) {
            throw new RuntimeException("Error: Este libro ya ha sido prestado a otra persona o no está disponible.");
        }

        int activos = prestamoRepository.contarPrestamosActivos(prestamo.getIdUsuario());
        if (activos >= 5) {
            throw new RuntimeException("Límite excedido: Ya tienes 5 préstamos activos. Devuelve uno primero.");
        }

        if (prestamo.getEstado() == null || prestamo.getEstado().isEmpty()) {
            prestamo.setEstado("ACTIVO"); 
        }

        Prestamo nuevo = prestamoRepository.save(prestamo);

        if (nuevo != null && nuevo.getIdPrestamo() != null) {
            prestamoRepository.actualizarEstadoEjemplar(prestamo.getIdEjemplar(), "Prestado");
        }

        return nuevo;
    }
    
    public List<Prestamo> getPrestamosByUsuario(Long idUsuario) {
        return prestamoRepository.findByUsuario(idUsuario);
    }

    public Prestamo updatePrestamo(Long id, Prestamo prestamo) {
        prestamo.setIdPrestamo(id);
        return prestamoRepository.update(prestamo);
    }

    public boolean deletePrestamo(Long idPrestamo) {
        // 1. Primero averiguamos qué ejemplar era para liberarlo
        Prestamo p = prestamoRepository.findById(idPrestamo);
        
        if (p != null) {
            boolean eliminado = prestamoRepository.delete(idPrestamo);
            if (eliminado) {
                // 2. Si se borró el préstamo, ponemos el libro como "Disponible" de nuevo
                prestamoRepository.actualizarEstadoEjemplar(p.getIdEjemplar(), "Disponible");
                return true;
            }
        }
        return false;
    }
}
