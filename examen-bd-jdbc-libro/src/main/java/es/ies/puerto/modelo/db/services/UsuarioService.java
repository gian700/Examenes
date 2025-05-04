package es.ies.puerto.modelo.db.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ies.puerto.modelo.db.entidades.Usuario;

public class UsuarioService extends DatabaseManager{

    public UsuarioService() {
        super();
    }

    /**
     * Metodo que interactua con la base de datos
     * @param sql
     * @return List<Usuario>
     */
    protected List<Usuario> buscarUsuario(String sql) {
        ArrayList<Usuario> usuario = new ArrayList<Usuario>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String idUsuarioStr = resultado.getString("id_usuario");
                String nombreStr = resultado.getString("nombre");
                String emailStr = resultado.getString("email");
                String telefonoStr = resultado.getString("telefono");
                String fechaStr = resultado.getString("fecha_registro");
                Date fecha = null;
                try {
                    fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                } catch (Exception e) {}
                Usuario UsuarioModel = new Usuario(idUsuarioStr, nombreStr, emailStr, telefonoStr, fecha);
                usuario.add(UsuarioModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { 
            cerrar();
        }
        return usuario;    
    }

    /**
     * Metodo que crea un nuevo Usuario
     * @param Usuario
     * @return boolean
     */
    public boolean crearUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        String query = "INSERT INTO usuarios(id_usuario, nombre, email, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?)";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, usuario.getIdUsuario());
            pStatement.setString(2, usuario.getNombre());
            pStatement.setString(3, usuario.getEmail());
            pStatement.setString(4, usuario.getTelefono());
            if (usuario.getFechaRegistro() != null) {
                pStatement.setString(5, formato.format(usuario.getFechaRegistro()));
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
     * Metodo que da todos los usuarios
     * @return List<Usuario>
     */
    public List<Usuario> obtenerTodosUsuarios() {
        String sql = "SELECT * FROM usuarios";
        return buscarUsuario(sql);
    }

    /**
     * Metodo que obtiene un usuario por id
     * @param dni
     * @return Usuario
     */
    public Usuario obtenerUsuarioPorId(String idUsuario) {
        String sql = "SELECT * FROM usuarios where id_usuario='"+idUsuario+"'";
        List<Usuario> usuarios = buscarUsuario(sql);
        if (usuarios.isEmpty()) {
            return null;
        }
        return usuarios.get(0);
    }

    /**
     * Metodo que actualiza la tabla autores
     * @param usuario
     * @return boolean
     */
    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            return false;
        }
        String query = "update usuarios set nombre=?, email=?, telefono=?, fecha_registro=? where id_usuario=?";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(5, usuario.getIdUsuario());
            pStatement.setString(1, usuario.getNombre());
            pStatement.setString(2, usuario.getEmail());
            pStatement.setString(3, usuario.getTelefono());
            if (usuario.getFechaRegistro() != null) {
                pStatement.setString(4, formato.format(usuario.getFechaRegistro()));
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
     * Metodo que elimina un usuario
     * @param idUsuario
     * @return boolean
     */
    public boolean eliminarUsuario(String idUsuario) {
        String query = "DELETE FROM usuarios WHERE id_usuario=?";
        try {
            PreparedStatement pStatement = conectar().prepareStatement(query);
            pStatement.setString(1, idUsuario);
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
