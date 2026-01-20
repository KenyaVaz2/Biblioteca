package com.library.features.editorial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.editorial.repository.EditorialRepository;
import com.library.model.Editorial;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository; 

    public Editorial findById(Long id){
        return editorialRepository.findById(id);
    }

    public List<Editorial> findAll(){
        return editorialRepository.findAll(); 
    }

    public Editorial save(Editorial editorial){
        return editorialRepository.save(editorial);
    }

    public Editorial update(Editorial editorial){
        return editorialRepository.update(editorial);
    }

    public boolean deleteEditorial(Long id){
        return editorialRepository.deleteEditorial(id);
    }

}
