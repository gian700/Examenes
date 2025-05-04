package es.ies.puerto.modelo.db.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.ies.puerto.config.ConfigManager;

public abstract class DatabaseManager {
    
    private String databasePath;
    private File file;
    private Connection connection;

    /**
     * Constructor general.
     * 
     * @throws SQLException error controlado.
     */

    protected DatabaseManager() {
        ConfigManager.ConfigProperties.setPath("src/main/resources/app.properties");
        databasePath = ConfigManager.ConfigProperties.getProperty("ruta");
        file = new File(databasePath);
        try {
            if (!file.exists()) {
                throw new SQLException("No existe la base de datos: " + databasePath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this.connection;
    }


    /**
     * Funcion que abre la conexion a la bbdd
     * @return
     * @throws SQLException
     */
    public Connection conectar() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        }
        return connection;
    }

    /**
     * Funcion que cierra la conexion de bbdd
     * @throws SQLException
     */
    public void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            connection = null;
        } catch (Exception e) {
        }
    }
}
