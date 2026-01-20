package com.library.features.autor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.autor.repository.AutorRepository;
import com.library.model.Autor;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository; 

    public Autor getAutorById(Long id){
        return autorRepository.findById(id);
    }

    public List<Autor> getAllAutores() {
        return autorRepository.findAll(); 
    }

    public Autor createAutor(Autor autor){
        return autorRepository.save(autor); 
    }

    public Autor updateAutor(Long id, Autor autor){
        autor.setIdAutor(id);
        return autorRepository.update(autor);
    }

    public boolean deleteAutor(Long id){
        return autorRepository.deleteAutor(id);
    }
    
}