package com.library.features.multa.repository;

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
import com.library.model.Multa;
import com.library.model.Prestamo;
import com.library.model.Usuario;

@Repository
public class MultaRepo {

    @Autowired
    private DataSource dataSource;

    public void marcarComoPagada(Integer idMulta) {
    String sql = "UPDATE MULTA SET Estado_Pago = 'Pagado' WHERE ID_Multa = ?";
    try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idMulta);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error al pagar multa: " + e.getMessage());
    }
}

    // BUSCAR MULTAS POR ID DE USUARIO
    public List<Multa> findByUsuario(Long idUsuario) {
        List<Multa> multas = new ArrayList<>();

        // Hacemos un INNER JOIN para conectar la Multa con el Préstamo y filtrar por Usuario
        String sql = "SELECT m.ID_Multa, m.Monto, m.Estado_Pago, m.ID_Prestamo " +
                    "FROM MULTA m " +
                    "INNER JOIN PRESTAMO p ON m.ID_Prestamo = p.ID_Prestamo " +
                    "WHERE p.ID_Usuario = ?";

        try (Connection conn = dataSource.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Multa multa = new Multa();
                multa.setIdMulta(rs.getInt("ID_Multa"));
                multa.setMonto(rs.getFloat("Monto"));
                multa.setEstadoPago(rs.getString("Estado_Pago"));

                // Reconstruimos el objeto Prestamo (solo el ID) para que no sea null
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getLong("ID_Prestamo"));
                multa.setPrestamo(prestamo);

                multas.add(multa);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener multas por usuario: " + e.getMessage());
        }

        return multas;
    }

    // BUSCAR POR ID
    public Multa findById(Integer id) {

        Multa multa = null;

        String sql = "SELECT ID_Multa, Monto, Estado_Pago, ID_Prestamo FROM MULTA WHERE ID_Multa = ?";

        try (Connection conn = dataSource.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                multa = new Multa();

                multa.setIdMulta(rs.getInt("ID_Multa"));
                multa.setMonto(rs.getFloat("Monto"));
                multa.setEstadoPago(rs.getString("Estado_Pago"));

                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getLong("ID_Prestamo"));
                multa.setPrestamo(prestamo);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener multa por ID: " + e.getMessage());
        }

        return multa;
    }

    // LISTAR TODAS
    public List<Multa> findAll() {

        List<Multa> multas = new ArrayList<>();

        String sql = "SELECT m.ID_Multa, m.Monto, m.Estado_Pago, " +
                    "p.ID_Prestamo, u.Nombre AS NombreUsuario, l.Titulo AS TituloLibro " +
                    "FROM MULTA m " +
                    "INNER JOIN PRESTAMO p ON m.ID_Prestamo = p.ID_Prestamo " +
                    "INNER JOIN USUARIO u ON p.ID_Usuario = u.ID_Usuario " +
                    "INNER JOIN EJEMPLAR e ON p.ID_Ejemplar = e.ID_Ejemplar " +
                    "INNER JOIN LIBRO l ON e.ID_Libro = l.ID_Libro";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Multa multa = new Multa();

                multa.setIdMulta(rs.getInt("ID_Multa"));
                multa.setMonto(rs.getFloat("Monto"));
                multa.setEstadoPago(rs.getString("Estado_Pago"));

                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getLong("ID_Prestamo"));
                multa.setPrestamo(prestamo);

                Usuario u = new Usuario();
                u.setNombre(rs.getString("NombreUsuario")); // Nombre del deudor
                prestamo.setUsuario(u);

                Ejemplar e = new Ejemplar();
                Libro l = new Libro();
                l.setTitulo(rs.getString("TituloLibro")); // Título del libro
                e.setLibro(l);
                prestamo.setEjemplar(e);

                multas.add(multa);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener multas: " + e.getMessage());
        }

        return multas;
    }

    // CREAR
    public Multa save(Multa multa) {

        String sql = "INSERT INTO MULTA (Monto, Estado_Pago, ID_Prestamo)"+"VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setFloat(1, multa.getMonto());
            ps.setString(2, multa.getEstadoPago());
            ps.setLong(3, multa.getPrestamo().getIdPrestamo());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                multa.setIdMulta(rs.getInt(1));
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al guardar multa: " + e.getMessage());
        }

        return multa;
    }

    // ACTUALIZAR
    public Multa update(Multa multa) {

        String sql = "UPDATE MULTA SET Monto = ?, Estado_Pago = ?, ID_Prestamo = ? WHERE ID_Multa = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setFloat(1, multa.getMonto());
            ps.setString(2, multa.getEstadoPago());
            ps.setLong(3, multa.getPrestamo().getIdPrestamo());
            ps.setInt(4, multa.getIdMulta());

            int filas = ps.executeUpdate();
            if (filas == 0) return null;

        } catch (SQLException e) {
            System.out.println("Error al actualizar multa: " + e.getMessage());
            return null;
        }

        return multa;
    }

    // ELIMINAR
    public boolean delete(Integer id) {

        String sql = "DELETE FROM MULTA WHERE ID_Multa = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar multa: " + e.getMessage());
            return false;
        }
    }
}
