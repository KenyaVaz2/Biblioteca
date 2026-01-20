package com.library.features.prestamo.repository;

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
import com.library.model.Prestamo;
import com.library.model.Usuario;

@Repository
public class PrestamoRepo {

    @Autowired
    private DataSource dataSource;

    public int contarPrestamosActivos(Long idUsuario) {
        // Asumiendo que si está en la tabla es porque está activo
        String sql = "SELECT COUNT(*) FROM PRESTAMO WHERE ID_Usuario = ?";
        try (Connection conn = dataSource.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Prestamo> findByUsuario(Long idUsuario) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM PRESTAMO WHERE ID_Usuario = ?";
        try (Connection conn = dataSource.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getLong("ID_Prestamo"));
                p.setFechaInicio(rs.getDate("Fecha_Inicio"));
                p.setFechaDevolucion(rs.getDate("Fecha_Devolucion"));
                p.setIdUsuario(rs.getLong("ID_Usuario"));
                p.setIdEjemplar(rs.getLong("ID_Ejemplar"));
                prestamos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public void actualizarEstadoEjemplar(Long idEjemplar, String nuevoEstado) {
        String sql = "UPDATE EJEMPLAR SET Estado = ? WHERE ID_Ejemplar = ?";
        try (Connection conn = dataSource.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nuevoEstado);
            ps.setLong(2, idEjemplar);
            
            int filas = ps.executeUpdate();
            
            // Agrega esto temporalmente para verificar en consola
            System.out.println("Actualizando Ejemplar ID " + idEjemplar + " a estado: " + nuevoEstado + ". Filas afectadas: " + filas);
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado del ejemplar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean esEjemplarDisponible(Long idEjemplar) {
        String sql = "SELECT Estado FROM EJEMPLAR WHERE ID_Ejemplar = ?";
        try (Connection conn = dataSource.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idEjemplar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String estado = rs.getString("Estado");
                // Retorna TRUE solo si dice exactamente "Disponible"
                return "Disponible".equalsIgnoreCase(estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Si hay error o no existe, asumimos que no está disponible por seguridad
    }

    public Prestamo findById(Long id) {

        Prestamo prestamo = null;

        String sql = "SELECT ID_Prestamo, Fecha_Inicio, Fecha_Devolucion, ID_Usuario, ID_Ejemplar FROM PRESTAMO WHERE ID_Prestamo = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prestamo = new Prestamo();

                prestamo.setIdPrestamo(rs.getLong("ID_Prestamo"));
                prestamo.setFechaInicio(rs.getDate("Fecha_Inicio"));
                prestamo.setFechaDevolucion(rs.getDate("Fecha_Devolucion"));
                prestamo.setIdUsuario(rs.getLong("ID_Usuario"));
                prestamo.setIdEjemplar(rs.getLong("ID_Ejemplar"));
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener préstamo por ID: " + e.getMessage());
        }

        return prestamo;
    }

public List<Prestamo> findAll() {
    List<Prestamo> prestamos = new ArrayList<>();
    
    String sql = "SELECT p.ID_Prestamo, p.Fecha_Inicio, p.Fecha_Devolucion, p.Estado, " +
                    "u.ID_Usuario, u.Nombre, " + 
                    "e.ID_Ejemplar, l.Titulo " +   
                    "FROM PRESTAMO p " +
                    "INNER JOIN USUARIO u ON p.ID_Usuario = u.ID_Usuario " +
                    "INNER JOIN EJEMPLAR e ON p.ID_Ejemplar = e.ID_Ejemplar " +
                    "INNER JOIN LIBRO l ON e.ID_Libro = l.ID_Libro";

    try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Prestamo p = new Prestamo();
            p.setIdPrestamo(rs.getLong("ID_Prestamo"));
            p.setFechaInicio(rs.getDate("Fecha_Inicio"));
            p.setFechaDevolucion(rs.getDate("Fecha_Devolucion"));
            p.setEstado(rs.getString("Estado"));

           // --- LLENAMOS EL OBJETO USUARIO ---
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getLong("ID_Usuario"));
            u.setNombre(rs.getString("Nombre"));
            p.setUsuario(u); 
            p.setIdUsuario(u.getIdUsuario());

            // Rellenamos el Ejemplar y el Libro con el título
            Ejemplar e = new Ejemplar();
            e.setID_Ejemplar(rs.getLong("ID_Ejemplar"));
            
            Libro l = new Libro();
            l.setTitulo(rs.getString("Titulo")); 
            e.setLibro(l);
            
            p.setEjemplar(e);
            p.setIdEjemplar(e.getID_Ejemplar());

            prestamos.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return prestamos;
}

    // CREAR
    public Prestamo save(Prestamo prestamo) {

        // 1. Agregamos 'Estado' a la lista de columnas y un '?' más a los valores
        String sql = "INSERT INTO PRESTAMO (Fecha_Inicio, Fecha_Devolucion, ID_Usuario, ID_Ejemplar, Estado) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, prestamo.getFechaInicio());
            ps.setDate(2, prestamo.getFechaDevolucion());
            ps.setLong(3, prestamo.getIdUsuario());
            ps.setLong(4, prestamo.getIdEjemplar());
            
            // 2. Insertamos el Estado (que el Service ya debió poner como "ACTIVO")
            ps.setString(5, prestamo.getEstado()); 

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        prestamo.setIdPrestamo(rs.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error crítico al guardar préstamo: " + e.getMessage());
            // Opcional: Lanzar una excepción para que el Service sepa que falló
            throw new RuntimeException("Error al guardar en BD: " + e.getMessage());
        }

        return prestamo;
    }
    
    // ACTUALIZAR
    public Prestamo update(Prestamo prestamo) {

        String sql = "UPDATE PRESTAMO SET Fecha_Inicio = ?, Fecha_Devolucion = ?, ID_Usuario = ?, ID_Ejemplar = ? WHERE ID_Prestamo = ?";

        try (Connection conn = dataSource.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, prestamo.getFechaInicio());
            ps.setDate(2, prestamo.getFechaDevolucion());
            ps.setLong(3, prestamo.getIdUsuario());
            ps.setLong(4, prestamo.getIdEjemplar());
            ps.setLong(5, prestamo.getIdPrestamo());

            int filas = ps.executeUpdate();
            if (filas == 0) return null;

        } catch (SQLException e) {
            System.out.println("Error al actualizar préstamo: " + e.getMessage());
            return null;
        }

        return prestamo;
    }

    // ELIMINAR
    public boolean delete(Long id) {

        String sql = "DELETE FROM PRESTAMO WHERE ID_Prestamo = ?";

        try (Connection conn = dataSource.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }
}
