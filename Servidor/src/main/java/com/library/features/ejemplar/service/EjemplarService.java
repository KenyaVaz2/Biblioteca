package com.library.features.ejemplar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.ejemplar.repository.EjemplarRepo;
import com.library.model.Ejemplar;


@Service
public class EjemplarService {


    @Autowired
    private EjemplarRepo ejemplarRepo; 

    public List<Ejemplar> getEjemplaresPorLibro(Long idLibro) {
        return ejemplarRepo.buscarPorIdLibro(idLibro);
    }

    public List<Ejemplar> getEncontrartodos(){
        return ejemplarRepo.getEncontrartodos();
    }

    public Ejemplar getporID(long id){
        return ejemplarRepo.getporID(id);
    }

    public Ejemplar updatEjemplar(Ejemplar ejemplar){
        return ejemplarRepo.updateEjemplar(ejemplar);
    }

    public Ejemplar crearEjemplar(Ejemplar ejemplar){
        return ejemplarRepo.crearEjemplar(ejemplar); 
    }

    public boolean eliminarEjemplar(Long id){
        return ejemplarRepo.eliminarEjemplar(id);
    }
}
