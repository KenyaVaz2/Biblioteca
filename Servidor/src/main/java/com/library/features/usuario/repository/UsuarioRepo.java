package com.library.features.usuario.repository;

/* CAPA QUE SE CONECTA DIRECTAMENTE CON LA BASE DE DATOS */
/* NO USAR JPA REPOSITORY */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.model.Usuario;

@Repository
public class UsuarioRepo {

    @Autowired
    private DataSource dataSource;

    // BUSCAR POR ID
    public Usuario findById(Long id) {

        Usuario usuario = null;

        String sql = "SELECT ID_Usuario, Nombre, Contraseña, Correo, Rol FROM USUARIO WHERE ID_Usuario = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getLong("ID_Usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setPassword(rs.getString("Contraseña"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setRol(rs.getString("Rol"));
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por ID: " + e.getMessage());
        }

        return usuario;
    }

    // LISTAR TODOS
    public List<Usuario> findAll() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT ID_Usuario, Nombre, Contraseña, Correo, Rol FROM USUARIO";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getLong("ID_Usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setPassword(rs.getString("Contraseña"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setRol(rs.getString("Rol"));

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    // CREAR USUARIO
    public Usuario save(Usuario usuario) {

        String sql = "INSERT INTO USUARIO (Nombre, Contraseña, Correo, Rol)" + "VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getRol());

            ps.setString(4, usuario.getRol());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setIdUsuario(rs.getLong(1));
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            throw new RuntimeException("Error al crear usuario "+ e.getMessage());
        }

        return usuario;
    }

    // ACTUALIZAR USUARIO
    public Usuario update(Usuario usuario) {

        String sql = "UPDATE USUARIO SET Nombre = ?, Contraseña = ?, Correo = ?, Rol = ? WHERE ID_Usuario = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getRol());
            ps.setLong(5, usuario.getIdUsuario());

            int filas = ps.executeUpdate();
            if (filas == 0)
                return null;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return null;
        }

        return usuario;
    }

    // ELIMINAR USUARIO
    public boolean delete(Long id) {

        String sql = "DELETE FROM USUARIO WHERE ID_Usuario = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean existsByCorreo(String correo) {

        String sql = "SELECT 1 FROM USUARIO WHERE Correo = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true si existe, false si no

        } catch (Exception e) {
            System.out.println("Error al validar correo: " + e.getMessage());
            return false;
        }
    }

    public boolean existsById(Long id) {

        String sql = "SELECT 1 FROM USUARIO WHERE ID_Usuario = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true si existe, false si no

        } catch (Exception e) {
            System.out.println("Error al verificar existencia de usuario por ID: " + e.getMessage());
            return false;
        }
    }

    public Optional<Usuario> findByCorreo(String correo) {

        String sql = "SELECT ID_Usuario, Nombre, Contraseña, Correo, Rol FROM USUARIO WHERE Correo = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getLong("ID_Usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setPassword(rs.getString("Contraseña"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setRol(rs.getString("Rol"));

                return Optional.of(usuario);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar usuario por correo" + e.getMessage());
        }

        return Optional.empty();

    }

}
