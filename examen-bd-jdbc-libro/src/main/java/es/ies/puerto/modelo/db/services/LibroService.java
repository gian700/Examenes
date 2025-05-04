package es.ies.puerto.modelo.db.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ies.puerto.modelo.db.entidades.Libro;

public class LibroService extends DatabaseManager{

    public LibroService() {
        super();
    }

    /**
     * Metodo que interactua con la base de datos
     * @param sql
     * @return List<Libro>
     */
    protected List<Libro> buscarLibro(String sql) {
        ArrayList<Libro> Libro = new ArrayList<Libro>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                String idStr = resultado.getString("id_libro");
                String titulo = resultado.getString("titulo");
                String autorDni = resultado.getString("autor_dni");
                String fechaStr = resultado.getString("fecha_publicacion");
                Date fecha = null;
                try {
                    fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                } catch (Exception e) {}
                String generoStr = resultado.getString("genero");
                Libro LibroModel = new Libro(idStr, titulo, autorDni, fecha, generoStr);
                Libro.add(LibroModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
            cerrar();
        }
        return Libro;    
    }

    /**
     * Metodo que crea un nuevo libro
     * @param libro
     * @return boolean
     */
    public boolean crearLibro(Libro libro) {
        if (libro == null) {
            return false;
        }
        String query = "INSERT INTO libros(id_libro, titulo, autor_dni, fecha_publicacion, genero) VALUES (?, ?, ?, ?, ?)";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, libro.getIdLibro());
            pStatement.setString(2, libro.getTitulo());
            pStatement.setString(3, libro.getAutorDni());
            if (libro.getFechaPublicacion() != null) {
                pStatement.setString(4, formato.format(libro.getFechaPublicacion()));
            } else {
                pStatement.setString(4, null);
            }
            pStatement.setString(5, libro.getGenero());
            if (pStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            cerrar();
        }
        return false;
    }

    /**
     * Metodo que da todos los libros
     * @return List<Libro>
     */
    public List<Libro> obtenerTodosLibros() {
        String sql = "SELECT * FROM libros";
        return buscarLibro(sql);
    }

    /**
     * Metodo que obtiene un autor por id
     * @param dni
     * @return Libro
     */
    public Libro obtenerLibroPorId(String idLibro) {
        String sql = "SELECT * FROM libros where id_libro='"+idLibro+"'";
        List<Libro> libros = buscarLibro(sql);
        if (libros.isEmpty()) {
            return null;
        }
        return libros.get(0);
    }

    /**
     * Metodo que actualiza la tabla libros
     * @param libro
     * @return boolean
     */
    public boolean actualizarLibro(Libro libro) {
        if (libro == null || libro.getIdLibro() == null) {
            return false;
        }
        String query = "update libros set titulo=?, autor_dni=?, fecha_publicacion=?, genero=? where id_libro=?";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(5, libro.getIdLibro());
            pStatement.setString(1, libro.getTitulo());
            pStatement.setString(2, libro.getAutorDni());
            if (libro.getFechaPublicacion() != null) {
                pStatement.setString(4, formato.format(libro.getFechaPublicacion()));
            } else {
                pStatement.setString(4, null);
            }
            pStatement.setString(4, libro.getGenero());
            if (pStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            cerrar();
        }
        return false;
    }

    /**
     * Metodo que elimina un libro
     * @param idLibro
     * @return boolean
     */
    public boolean eliminarLibro(String idLibro) {
        String query = "DELETE FROM libros WHERE id_libro=?";
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, idLibro);
            if (pStatement.executeUpdate() == 1) {
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            cerrar();
        }
        return false;
    }

}
