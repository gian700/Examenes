package es.ies.puerto.modelo.db.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ies.puerto.modelo.db.entidades.Autor;


public class AutorService extends DatabaseManager{

    public AutorService() {
        super();
    }

    /**
     * Metodo que interactua con la base de datos
     * @param sql
     * @return List<Autor>
     */
    protected List<Autor> buscarAutor(String sql) {
        ArrayList<Autor> autor = new ArrayList<Autor>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String dniStr = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String nacionalidadStr = resultado.getString("nacionalidad");
                String fechaStr = resultado.getString("fecha_nacimiento");
                Date fecha=null;
                try {
                    fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                } catch (Exception e) {}
                Autor AutorModel = new Autor(dniStr, nombre, nacionalidadStr, fecha);
                autor.add(AutorModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
            cerrar();
        }
        return autor;    
    }

    /**
     * Metodo que crea un nuevo autor
     * @param autor
     * @return boolean
     */
    public boolean crearAutor(Autor autor) {
        if (autor == null) {
            return false;
        }
        String query = "INSERT INTO autores(dni, nombre, nacionalidad, fecha_nacimiento) VALUES (?, ?, ?, ?)";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, autor.getDni());
            pStatement.setString(2, autor.getNombre());
            pStatement.setString(3, autor.getNacionalidad());
            if (autor.getFechaNacimiento() != null) {
                pStatement.setString(4, formato.format(autor.getFechaNacimiento()));
            } else {
                pStatement.setString(4, null);
            }
            
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
     * Metodo que da todos los autores
     * @return List<Autor>
     */
    public List<Autor> obtenerTodosAutores() { 
        String sql = "SELECT * FROM autores";
        return buscarAutor(sql);
    }

    /**
     * Metodo que obtiene un autor por dni
     * @param dni
     * @return Autor
     */
    public Autor obtenerAutorPorDni(String dni) {
        String sql = "SELECT * FROM autores where dni='"+dni+"'";
        List<Autor> autores = buscarAutor(sql);
        if (autores.isEmpty()) {
            return null;
        }
        return autores.get(0);
    }

    /**
     * Metodo que actualiza la tabla autores
     * @param autor
     * @return boolean
     */
    public boolean actualizarAutor(Autor autor) {
        if (autor == null || autor.getDni() == null) {
            return false;
        }
        String query = "update autores set nombre=?, nacionalidad=?, fecha_nacimiento=? where dni=?";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(4, autor.getDni());
            pStatement.setString(1, autor.getNombre());
            pStatement.setString(2, autor.getNacionalidad());
            if (autor.getFechaNacimiento() != null) {
                pStatement.setString(3, formato.format(autor.getFechaNacimiento()));
            } else {
                pStatement.setString(3, null);
            }
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
     * Metodo que elimina un autor
     * @param dni
     * @return boolean
     */
    public boolean eliminarAutor(String dni) {
        String query = "DELETE FROM autores WHERE dni=?";
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, dni);
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
