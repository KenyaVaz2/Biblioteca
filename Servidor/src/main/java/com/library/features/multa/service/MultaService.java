package com.library.features.multa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.multa.repository.MultaRepo;
import com.library.features.prestamo.repository.PrestamoRepo;
import com.library.model.Multa;

@Service
public class MultaService {

    @Autowired
    private MultaRepo multaRepository;
    private PrestamoRepo prestamoRepository;

    public void pagarMulta(Integer idMulta) {
    multaRepository.marcarComoPagada(idMulta);

    Multa multa = multaRepository.findById(idMulta);
    if (multa != null && multa.getPrestamo() != null) {
        prestamoRepository.delete(multa.getPrestamo().getIdPrestamo());
    }
}

    public List<Multa> getMultasByUsuario(Long idUsuario) {
        return multaRepository.findByUsuario(idUsuario);
    }

    // OBTENER POR ID
    public Multa getMultaById(Integer id) {
        return multaRepository.findById(id);
    }

    // LISTAR TODAS
    public List<Multa> getAllMultas() {
        return multaRepository.findAll();
    }

    // CREAR
    public Multa createMulta(Multa multa) {
        return multaRepository.save(multa);
    }

    // ACTUALIZAR
    public Multa updateMulta(Integer id, Multa multa) {
        multa.setIdMulta(id); // importante
        return multaRepository.update(multa);
    }

    // ELIMINAR
    public boolean deleteMulta(Integer id) {
        return multaRepository.delete(id);
    }
}
