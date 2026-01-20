package com.library.features.LibroInter.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.model.LibroInter;

@Repository
public class LibroInterRepository {

    @Autowired
    private DataSource dataSource; 

    private final List<String> tablasExternas = List.of(
        "biblioteca_ESCOM.dbo.biblioteca_ESCOM",
        "biblioteca_ESFM.dbo.biblioteca_ESFM"
    );

    public List<LibroInter> buscarEnTodas(String titulo){
        List<LibroInter> resultadosGlobales = new ArrayList<>();

        for(String tabla : tablasExternas){
            String sql = "SELECT bookID, Titulo, Autor, ISBN, Editorial FROM " + tabla + " WHERE Titulo LIKE ?";

            try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1,"%" + titulo + "%");
                ResultSet rs = ps.executeQuery(); 

                while(rs.next()){

                    LibroInter libro = new LibroInter();

                    libro.setBookID(rs.getInt("bookID"));
                    libro.setTitulo(rs.getString("Titulo"));
                    libro.setAutor(rs.getString("Autor"));
                    libro.setIsbn(rs.getString("ISBN"));
                    libro.setEditorial(rs.getString("Editorial"));
                    libro.setFuente(tabla.replace("biblioteca_", ""));
                
                    resultadosGlobales.add(libro);
                }
                System.out.println("Busqueda exitosa en: " +tabla);
            } catch (SQLException e){
                System.err.println("Error consultando la tabla "+ tabla + ":" +e.getMessage());
            }
        }
        return resultadosGlobales;
        
    }
    
}
