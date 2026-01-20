package com.library.features.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.features.usuario.repository.UsuarioRepo;
import com.library.model.Usuario;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepo usuarioRepository;

    //LISTAR TODOS
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    //LISTAR POR ID
    public Usuario obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id);

    if (usuario == null) {
        throw new RuntimeException("Usuario no encontrado");
    }

    return usuario;
    }

    //CREAR USUARIO
    public Usuario crearUsuario(Usuario usuario) {

        // Validar correo único
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if(usuario.getRol() == null || usuario.getRol().isEmpty()){
            usuario.setRol("USUARIO");
        }

        return usuarioRepository.save(usuario);
    }

    //EDITAR USUARIO
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {

        Usuario usuarioExistente = obtenerUsuarioPorId(id);

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());

        return usuarioRepository.save(usuarioExistente);
    }

    //ELIMINAR USUARIO
    public void eliminarUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario no existe");
        }

        usuarioRepository.delete(id);
    }

    //LOGIN BÁSICO
    public Usuario login(String correo, String password) {

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Correo no registrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }

}
