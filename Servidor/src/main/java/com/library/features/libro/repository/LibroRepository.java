package com.library.features.libro.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.model.Autor;
import com.library.model.Editorial;
import com.library.model.Libro;

@Repository
public class LibroRepository {

    @Autowired
    private DataSource dataSource;

    // BUSCAR POR ID
    public Libro findById(Long id) {

        Libro libro = null;

        String sql = "SELECT ID_Libro, ISBN, Idioma, Fecha_Publicacion, ID_Editorial, Titulo, Genero FROM LIBRO WHERE ID_Libro = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                libro = mapearLibro(rs);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener libro por ID: " + e.getMessage());
        }

        return libro;
    }

    //BUSCAR POR AUTOR
    public List<Libro> findByAutorNombre(String nombreAutor){
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT L.ID_Libro, L.ISBN, L.Idioma, L.Fecha_Publicacion, L.ID_Editorial, L.Titulo, L.Genero " +
                    "FROM LIBRO L " +
                    "JOIN AUTOR_LIBRO AL ON L.ID_Libro = AL.ID_Libro " +
                    "JOIN AUTOR A ON AL.ID_Autor = A.ID_Autor " +
                    "WHERE A.Nombre LIKE ?";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreAutor + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                libros.add(mapearLibro(rs));
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor "+e.getMessage());
        }
        return libros;             
    }

    //BUSCAR POR ISBN 
    public Libro findByIsbn(String isbn) {

        Libro libro = null;

        String sql = "SELECT ID_Libro, ISBN, Idioma, Fecha_Publicacion, ID_Editorial, Titulo, Genero FROM LIBRO WHERE ISBN = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                libro = mapearLibro(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener libro por ISBN: " + e.getMessage());
        }
        return libro;
    }

    //BUSCAR POR TITULO
    public List<Libro> findByTitulo(String titulo){
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT ID_Libro, ISBN, Idioma, Fecha_Publicacion, ID_Editorial, Titulo, Genero FROM LIBRO WHERE Titulo LIKE ?";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                libros.add(mapearLibro(rs));
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor "+e.getMessage());
        }
        return libros;             
    }

    //BUSCAR POR GENERO
    public List<Libro> findByGenero(String genero){
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT ID_Libro, ISBN, Idioma, Fecha_Publicacion, ID_Editorial, Titulo, Genero FROM LIBRO WHERE Genero LIKE ?";

        try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + genero + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                libros.add(mapearLibro(rs));
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor "+e.getMessage());
        }
        return libros;             
    }

    // LISTAR TODOS
    public List<Libro> findAll() {

        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT ID_Libro, ISBN, Idioma, Fecha_Publicacion, ID_Editorial, Titulo, Genero FROM LIBRO";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                libros.add(mapearLibro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener libros: " + e.getMessage());
        }

        return libros;
    }

    // CREAR
    public Libro save(Libro libro) {

        String sqlLibro = "INSERT INTO LIBRO (ISBN, Idioma, Fecha_Publicacion, ID_Editorial, Titulo, Genero) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlRelacion = "INSERT INTO AUTOR_LIBRO (ID_Libro, ID_Autor) VALUES (?,?)";

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try(PreparedStatement ps = conn.prepareStatement(sqlLibro, PreparedStatement.RETURN_GENERATED_KEYS)) {
                
                ps.setString(1, libro.getIsbn());
                ps.setString(2, libro.getIdioma());
                ps.setDate(3, libro.getFechaPublicacion());
                ps.setLong(4, libro.getEditorial().getId());
                ps.setString(5, libro.getTitulo());
                ps.setString(6, libro.getGenero());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    libro.setId(rs.getLong(1));
                }

                if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                    try(PreparedStatement psRel = conn.prepareStatement(sqlRelacion)) {
                        for(Autor autor : libro.getAutores()){
                            psRel.setLong(1, libro.getId());
                            psRel.setLong(2, autor.getIdAutor());
                            psRel.addBatch();
                        }
                        psRel.executeBatch();
                    } 
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e; 
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar libro y sus relaciones: " + e.getMessage());
        }
        return libro;
    }

    // ACTUALIZAR
    public Libro update(Libro libro) {

        String sql = "UPDATE LIBRO SET ISBN = ?, Idioma = ?, Fecha_Publicacion = ?, ID_Editorial = ?, Titulo = ?, Genero = ? WHERE ID_Libro = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getIdioma());
            ps.setDate(3, libro.getFechaPublicacion());
            ps.setLong(4, libro.getEditorial().getId());
            ps.setString(5, libro.getTitulo());
            ps.setString(6, libro.getGenero());
            ps.setLong(7, libro.getId());

            int filas = ps.executeUpdate();
            if (filas == 0) return null;

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
            return null;
        }

        return libro;
    }

    // ELIMINAR 
    public boolean delete(Long id) {

        String sql = "DELETE FROM LIBRO WHERE ID_Libro = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    private List<Autor> obtenerAutoresPorLibroId(Long idLibro) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT A.ID_Autor, A.Nombre, A.Apellido FROM AUTOR A " +
                "JOIN AUTOR_LIBRO AL ON A.ID_Autor = AL.ID_Autor " +
                "WHERE AL.ID_Libro = ?";
    
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idLibro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setIdAutor(rs.getLong("ID_Autor"));
                autor.setNombre(rs.getString("Nombre"));
                autor.setApellido(rs.getString("Apellido"));
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener autores del libro: " + e.getMessage());
        }
        return autores;
    }

    // TIP: Crea un método privado para no repetir el mapeo en cada método
    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        long id = rs.getLong("ID_Libro");
        long idEditorial = rs.getLong("ID_Editorial");

        List<Autor> listaAutores = obtenerAutoresPorLibroId(id);
        
        libro.setId(rs.getLong("ID_Libro"));
        libro.setIsbn(rs.getString("ISBN"));
        libro.setIdioma(rs.getString("Idioma"));
        libro.setFechaPublicacion(rs.getDate("Fecha_Publicacion"));
        libro.setTitulo(rs.getString("Titulo"));
        libro.setGenero(rs.getString("Genero"));
        Editorial ed = new Editorial();
        ed.setId(rs.getLong("ID_Editorial"));
        libro.setEditorial(ed);
        libro.setAutores(new HashSet<>(listaAutores));
        libro.setEditorial(obtenerEditorialPorId(idEditorial));

        return libro;
    }
    
    private Editorial obtenerEditorialPorId(Long idEditorial) {
        Editorial editorial = new Editorial();
        String sql = "SELECT ID_Editorial, Nombre FROM EDITORIAL WHERE ID_Editorial = ?";

        try (Connection conn = dataSource.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setLong(1, idEditorial);
            ResultSet rs = ps.executeQuery();
        
            if (rs.next()) {
                editorial.setId(rs.getLong("ID_Editorial"));
                editorial.setNombre(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener nombre de editorial: " + e.getMessage());
        }
        return editorial;
    }

}
