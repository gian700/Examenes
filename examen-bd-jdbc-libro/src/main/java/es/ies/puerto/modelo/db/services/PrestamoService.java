package es.ies.puerto.modelo.db.services;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ies.puerto.modelo.db.entidades.Prestamo;

public class PrestamoService extends DatabaseManager{

    public PrestamoService() {
        super();
    }

    /**
     * Metodo que interactua con la base de datos
     * @param sql
     * @return List<Prestamo>
     */
    protected List<Prestamo> buscarPrestamo(String sql) {
        ArrayList<Prestamo> prestamo = new ArrayList<Prestamo>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String prestamoId = resultado.getString("id_prestamo");
                String libroId = resultado.getString("libro_id");
                String usuarioID = resultado.getString("usuario_id");

                String fechaStr = resultado.getString("fecha_prestamo");
                Date fecha1 = null;
                try {
                    fecha1 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                } catch (Exception e) {}
                String fechaStr2 = resultado.getString("fecha_devolucion");
                Date fecha2 = null;
                try {
                    fecha2 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr2);
                } catch (Exception e) {}
                Prestamo prestamoModel = new Prestamo(prestamoId, libroId, usuarioID, fecha1, fecha2);
                prestamo.add(prestamoModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
            cerrar();
        }
        return prestamo;    
    }

    /**
     * Metodo que crea un nuevo Prestamo
     * @param Prestamo
     * @return boolean
     */
    public boolean crearPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            return false;
        }
        String query = "INSERT INTO prestamos(id_prestamo, libro_id, usuario_id, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?, ?)";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, prestamo.getIdPrestamo());
            pStatement.setString(2, prestamo.getLibroId());
            pStatement.setString(3, prestamo.getUsuarioId());
            if (prestamo.getFechaPrestamo() != null) {
                pStatement.setString(4, formato.format(prestamo.getFechaPrestamo()));
            } else {
                pStatement.setString(4, null);
            }
            if (prestamo.getFechaDevolucion() != null) {
                pStatement.setString(5, formato.format(prestamo.getFechaDevolucion()));
            } else {
                pStatement.setString(5, null);
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
     * Metodo que da todos los prestamos
     * @return List<Prestamo>
     */
    public List<Prestamo> obtenerTodosPrestamos() {
        String sql = "SELECT * FROM prestamos";
        return buscarPrestamo(sql);
    }

    /**
     * Metodo que obtiene un prestamo por id
     * @param dni
     * @return Prestamo
     */
    public Prestamo obtenerPrestamoPorId(String id_prestamo) {
        String sql = "SELECT * FROM prestamos where id_prestamo='"+id_prestamo+"'";
        List<Prestamo> prestamos = buscarPrestamo(sql);
        if (prestamos.isEmpty()) {
            return null;
        }
        return prestamos.get(0);
    }

    /**
     * Metodo que actualiza la tabla prestamos
     * @param prestamo
     * @return boolean
     */
    public boolean actualizarPrestamo(Prestamo prestamo) {
        if (prestamo == null || prestamo.getIdPrestamo() == null) {
            return false;
        }
        String query = "update prestamos set libro_id=?, usuario_id=?, fecha_prestamo=?, fecha_devolucion=? where id_prestamo=?";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(5, prestamo.getIdPrestamo());
            pStatement.setString(1, prestamo.getLibroId());
            pStatement.setString(2, prestamo.getUsuarioId());
            if (prestamo.getFechaPrestamo() != null) {
                pStatement.setString(3, formato.format(prestamo.getFechaPrestamo()));
            } else {
                pStatement.setString(3, null);
            }
            if (prestamo.getFechaDevolucion() != null) {
                pStatement.setString(4, formato.format(prestamo.getFechaDevolucion()));
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
     * Metodo que elimina un prestamo
     * @param id
     * @return boolean
     */
    public boolean eliminarPrestamo(String id) {
        String query = "DELETE FROM prestamos WHERE id_prestamo=?";
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, id);
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


    /**
     * Metodo que da todos los libros vencidos a partir de una fecha
     * @param fechaLimite
     * @return List<Prestamo>
     */
    public List<Prestamo> obtenerPrestamosVencidos(Date fechaLimite) {
        List<Prestamo> resultado = new ArrayList<>();
        
        return resultado;
    }


    /**
     * Metodo que da los prestamos que tiene un usuario
     * @param idUsuario
     * @return List<Prestamo>
     */
    public List<Prestamo> obtenerPrestamosPorUsuario(String idUsuario) {
        String sql = "SELECT * FROM prestamos where usuario_id='"+idUsuario+"'";
        return buscarPrestamo(sql);
    }


    /**
     * Metodo que da los prestamos activos
     * @return List<Prestamo>
     */
    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> resultado = new ArrayList<>();
        

        return resultado;
    }


}

