package com.library.features.editorial.repository;
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
import com.library.model.Editorial;

@Repository
public class EditorialRepository {

    @Autowired
    private DataSource dataSource; 

    public Editorial findById(Long id){

        Editorial editorial = null;

        String sql = "SELECT ID_Editorial, Nombre, Direccion, Telefono, SitioWeb FROM EDITORIAL WHERE ID_Editorial = ?";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
            
                editorial = new Editorial();
                editorial.setId(rs.getLong("ID_Editorial"));
                editorial.setNombre(rs.getString("Nombre"));
                editorial.setDireccion(rs.getString("Direccion")); 
                editorial.setTelefono(rs.getString("Telefono"));
                editorial.setSitioWeb(rs.getString("SitioWeb"));
            }

            rs.close();

        } catch (SQLException e){
            System.out.println("Error al obtener editorial por ID: "+e.getMessage());
        }
        
        return editorial;
    }

    public List<Editorial> findAll(){

        List<Editorial> editoriales = new ArrayList<>();

        String sql = "SELECT ID_Editorial, Nombre, Direccion, Telefono, SitioWeb FROM EDITORIAL";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery();){

            while (rs.next()) {
                Editorial editorial = new Editorial(); 

                editorial.setId(rs.getLong("ID_Editorial"));
                editorial.setNombre(rs.getString("Nombre"));
                editorial.setDireccion(rs.getString("Direccion"));
                editorial.setTelefono(rs.getString("Telefono"));
                editorial.setSitioWeb(rs.getString("SitioWeb"));

                editoriales.add(editorial);
            }

        } catch(SQLException e){
            System.out.println("Error al obtener Editoriales : "+e.getMessage());
        }
        return editoriales;
    }

    public Editorial save(Editorial editorial){

        String sql = "INSERT INTO EDITORIAL (Nombre, Direccion, Telefono, SitioWeb)" + "VALUES (?,?,?,?)";

        try( Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);){

            ps.setString(1, editorial.getNombre());
            ps.setString(2,editorial.getDireccion());
            ps.setString(3, editorial.getTelefono());
            ps.setString(4, editorial.getSitioWeb());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if(rs.next()){
                editorial.setId(rs.getLong(1));
            }

            rs.close();

        }catch (SQLException e){
            System.out.println("Error al guardar editorial "+e.getMessage());
        }

        return editorial; 
    }

    public Editorial update(Editorial editorial){
        String sql = "UPDATE EDITORIAL SET Nombre = ?, Direccion = ?, Telefono = ?, SitioWeb = ?  WHERE ID_Editorial = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, editorial.getNombre());
            ps.setString(2, editorial.getDireccion());
            ps.setString(3, editorial.getTelefono());
            ps.setString(4, editorial.getSitioWeb());
            ps.setLong(5, editorial.getId());
            
            int filas  = ps.executeUpdate(); 

            if(filas == 0){
                return null; // No se encontro el editorial  
            }
            
        } catch (Exception e) {
            System.out.println("Error al actualizar editorial: "+ e.getMessage());
            return null; 
        }

        return editorial; 
    }

    public boolean deleteEditorial(Long id){

        String sql = "DELETE FROM EDITORIAL WHERE ID_Editorial = ?"; 

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            

            ps.setLong(1, id);

            int fila = ps.executeUpdate(); 

            return fila > 0; 
            
        } catch (Exception e) {
            System.out.println("Error al eliminar el editorial" + e.getMessage());
            return false; 
        }
    }

}
