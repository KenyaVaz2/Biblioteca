package com.library.features.autor.repository;
/* CAPA QUE SE CONECTA DIRECTAMENTE CON LA BASE DE DATOS  */
/* NO USAR JPA REPOSITORY  */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.model.Autor;

@Repository
public class AutorRepository {

    @Autowired
    private DataSource dataSource; 

    public Autor findById(Long id){
        Autor autor = null;

        String sql = "SELECT ID_Autor, Nombre, Apellido, Nacionalidad, Fecha_Nac FROM AUTOR WHERE ID_Autor = ?";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
            
                autor = new Autor();
                autor.setIdAutor(rs.getLong("ID_Autor"));
                autor.setNombre(rs.getString("Nombre"));
                autor.setApellido(rs.getString("Apellido")); 
                autor.setNacionalidad(rs.getString("Nacionalidad"));
                autor.setFecha_nac(rs.getDate("Fecha_Nac"));
            }

            rs.close();

        } catch (SQLException e){
            System.out.println("Error al obtener autor por ID: "+e.getMessage());
        }
        
        return autor;
    }

    public List<Autor> findAll(){

        List<Autor> autores = new ArrayList<>();

        String sql = "SELECT ID_Autor, Nombre, Apellido, Nacionalidad, Fecha_Nac FROM AUTOR";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery();){

            while (rs.next()) {
                Autor autor = new Autor(); 

                autor.setIdAutor(rs.getLong("ID_Autor"));
                autor.setNombre(rs.getString("Nombre"));
                autor.setApellido(rs.getString("Apellido"));
                autor.setNacionalidad(rs.getString("Nacionalidad"));
                autor.setFecha_nac(rs.getDate("Fecha_Nac"));

                autores.add(autor);
            }

        } catch(SQLException e){
            System.out.println("Error al obtener autores : "+e.getMessage());
        }
        return autores;
    }

    public Autor save(Autor autor){

        String sql = "INSERT INTO AUTOR (Nombre, Apellido, Nacionalidad, Fecha_Nac)" + "VALUES (?,?,?,?)";

        try( Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);){

            ps.setString(1, autor.getNombre());
            ps.setString(2,autor.getApellido());
            ps.setString(3, autor.getNacionalidad());
            ps.setDate(4, autor.getFecha_nac());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if(rs.next()){
                autor.setIdAutor(rs.getLong(1));
            }

            rs.close();

        }catch (SQLException e){
            System.out.println("Error al guardar autor "+e.getMessage());
        }
        return autor; 
    }

    public Autor update(Autor autor){
        String sql = "UPDATE AUTOR SET Nombre = ?, Apellido = ?, Nacionalidad = ?, Fecha_Nac = ?  WHERE ID_Autor = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, autor.getNombre());
            ps.setString(2, autor.getApellido());
            ps.setString(3, autor.getNacionalidad());
            ps.setDate(4, autor.getFecha_nac());
            ps.setLong(5, autor.getIdAutor());
            
            int filas  = ps.executeUpdate(); 

            if(filas == 0){
                return null; // No se encontro el autor 
            }
            
        } catch (Exception e) {
            System.out.println("Error al actualizar autor: "+ e.getMessage());
            return null; 
        }

        return autor; 
    }

    public boolean deleteAutor(Long id){

        String sql = "DELETE FROM AUTOR WHERE ID_AUTOR = ?"; 

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1, id);

            int fila = ps.executeUpdate(); 
            return fila > 0; 
            
        } catch (Exception e) {
            System.out.println("Error al eliminar el autor" + e.getMessage());
            return false; 
        }
    }

}
