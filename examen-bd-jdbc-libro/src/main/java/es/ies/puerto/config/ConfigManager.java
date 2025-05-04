package es.ies.puerto.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigManager {
    /**
    * Clase estatica que maneja el idioma y guarda el usuario activo
    */
    public static class ConfigProperties {
        private static String path = "src/main/resources/app.properties";
        private static final Properties properties = new Properties();

        /**
         * Metodo estatico para obtener una propiedad
         **/
        public static String getProperty(String key) {
            return properties.getProperty(key);
        }

        /**
         * Metodo estatico para cambiar la ruta de propiedades
         **/
        public static void setPath(String ruta) {
            File file = new File(ruta);

            if (!file.exists() || !file.isFile()) {
                System.out.println("Path:"+file.getAbsolutePath());
            }
            path = ruta;
            try {
                FileInputStream input = new FileInputStream(path);
                InputStreamReader isr = new InputStreamReader(input, "UTF-8");
                properties.load(isr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String getPath() {
            return path;
        }

    }
}

