package com.library.features.ejemplar.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.model.Ejemplar;
import com.library.model.Libro;

@Repository
public class EjemplarRepo { 

    @Autowired
    private DataSource dataSource; 

    public List<Ejemplar> buscarPorIdLibro(Long idLibro) {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT ID_Ejemplar, Ubicacion, Estado, Cantidad FROM EJEMPLAR WHERE ID_Libro = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idLibro);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ejemplar ejemplar = new Ejemplar();

                ejemplar.setID_Ejemplar(rs.getLong("ID_Ejemplar"));
                ejemplar.setUbicacion(rs.getString("Ubicacion"));
                ejemplar.setEstado(rs.getString("Estado"));
                ejemplar.setCantidad(rs.getInt("Cantidad"));
                
                lista.add(ejemplar);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            System.err.println("Error buscando ejemplares para el libro: " + idLibro);
        }
        return lista;
    }

    //listar todos los ejemplares
    public List<Ejemplar> getEncontrartodos(){

        List<Ejemplar> ejemplares = new ArrayList<>();

        String sql = "SELECT ID_Ejemplar, Fecha_Compra, Ubicacion, Estado, ID_Libro, Cantidad FROM EJEMPLAR";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Ejemplar ejemplar = new Ejemplar();  

                ejemplar.setID_Ejemplar(rs.getLong("ID_Ejemplar"));
                ejemplar.setFecha_compra(rs.getDate("Fecha_Compra"));
                ejemplar.setUbicacion(rs.getString("Ubicacion"));
                ejemplar.setEstado(rs.getString("Estado"));
                ejemplar.setCantidad(rs.getInt("Cantidad"));

                Libro libro = new Libro(); 
                libro.setId(rs.getLong("ID_Libro"));
                ejemplar.setLibro(libro);

                ejemplares.add(ejemplar);
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println("Error al obtener ejemplares" +e.getMessage());
        }

        return ejemplares; 
    }

    //listar ejemplar por id. 
    public Ejemplar getporID(long id){ 

        Ejemplar ejemplar = null; 

        String sql = "SELECT ID_Ejemplar, Fecha_Compra, Ubicacion, Estado, ID_Libro, Cantidad FROM EJEMPLAR WHERE ID_EJEMPLAR = ?";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery(); 

            if(rs.next()){
                ejemplar = new Ejemplar();
                
                ejemplar.setID_Ejemplar(rs.getLong("ID_Ejemplar")); 
                ejemplar.setFecha_compra(rs.getDate("Fecha_Compra"));
                ejemplar.setUbicacion(rs.getString("Ubicacion"));
                ejemplar.setEstado(rs.getString("Estado"));
                ejemplar.setCantidad(rs.getInt("Cantidad"));
                
                Libro libro = new Libro(); 
                libro.setId(rs.getLong("ID_Libro"));
                ejemplar.setLibro(libro);
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println("Error al obtener ejemplar por ID"+ e.getMessage());
        }

        return ejemplar; 
    }

    //actualizar 
    public Ejemplar updateEjemplar(Ejemplar ejemplar){
        String sql = "UPDATE EJEMPLAR SET Fecha_Compra = ?, Ubicacion = ?, Estado = ?, ID_Libro = ?, Cantidad = ? WHERE ID_Ejemplar = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setDate(1, ejemplar.getFecha_compra());
            ps.setString(2, ejemplar.getUbicacion());
            ps.setString(3, ejemplar.getEstado());        
            ps.setLong(4, ejemplar.getLibro().getId());
            ps.setInt(5, ejemplar.getCantidad());
            ps.setLong(6, ejemplar.getID_Ejemplar());
            
            int filas  = ps.executeUpdate(); 

            if(filas == 0){
                return null; // No se encontro el autor 
            }

            return ejemplar;
            
        } catch (Exception e) {
            System.out.println("Error al actualizar ejemplar: "+ e.getMessage());
            return null; 
        }
    }

    //crear ejemplar 
    public Ejemplar crearEjemplar(Ejemplar ejemplar){

        String sql = "INSERT INTO EJEMPLAR (Fecha_Compra, Ubicacion, Estado, ID_Libro, Cantidad)" + "VALUES (?,?,?,?,?)";
        
        try( Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);){

            ps.setDate(1, ejemplar.getFecha_compra());
            ps.setString(2,ejemplar.getUbicacion());
            ps.setString(3, ejemplar.getEstado());
            ps.setLong(4, ejemplar.getLibro().getId());
            ps.setInt(5, ejemplar.getCantidad());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            if(rs.next()){
                ejemplar.setID_Ejemplar(rs.getLong(1));
            }

            rs.close();

        }catch (SQLException e){
            System.out.println("Error al guardar ejemplar "+e.getMessage());
        }

        return ejemplar; 
    }

    //eliminar ejemplar
    public boolean eliminarEjemplar(Long id){

        String sql = "DELETE FROM EJEMPLAR WHERE ID_Ejemplar = ?"; 

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1, id);

            int fila = ps.executeUpdate(); 

            return fila > 0; 
            
        } catch (Exception e) {
            System.out.println("Error al eliminar el ejemplar" + e.getMessage());
            return false; 
        }
    }

}

