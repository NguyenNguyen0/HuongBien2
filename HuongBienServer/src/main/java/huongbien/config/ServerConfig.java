package huongbien.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Server configuration manager for HuongBien Restaurant Management System.
 * Handles loading and accessing server configuration properties.
 */
public class ServerConfig {
    private static final Properties properties = new Properties();
    private static boolean isInitialized = false;
    
    // Default values
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final String DEFAULT_DB_URL = "jdbc:mariadb://localhost:3306/huongbien";
    private static final String DEFAULT_DB_USERNAME = "root";
    private static final String DEFAULT_DB_PASSWORD = "";
    
    /**
     * Loads the server configuration properties from the server.properties file.
     */
    public static void init() {
        if (isInitialized) {
            return;
        }
        
        try (InputStream is = ServerConfig.class.getClassLoader().getResourceAsStream("server.properties")) {
            if (is != null) {
                properties.load(is);
                System.out.println("Server configuration loaded successfully");
            } else {
                System.out.println("server.properties not found, using default values");
            }
        } catch (IOException e) {
            System.err.println("Error loading server configuration: " + e.getMessage());
            e.printStackTrace();
        }
        
        isInitialized = true;
    }
    
    /**
     * Gets the RMI port from the configuration.
     * 
     * @return The configured RMI port or the default value if not specified
     */
    public static int getRmiPort() {
        init();
        return Integer.parseInt(properties.getProperty("rmi.port", String.valueOf(DEFAULT_RMI_PORT)));
    }
    
    /**
     * Gets the database URL from the configuration.
     * 
     * @return The configured database URL or the default value if not specified
     */
    public static String getDbUrl() {
        init();
        return properties.getProperty("db.url", DEFAULT_DB_URL);
    }
    
    /**
     * Gets the database username from the configuration.
     * 
     * @return The configured database username or the default value if not specified
     */
    public static String getDbUsername() {
        init();
        return properties.getProperty("db.username", DEFAULT_DB_USERNAME);
    }
    
    /**
     * Gets the database password from the configuration.
     * 
     * @return The configured database password or the default value if not specified
     */
    public static String getDbPassword() {
        init();
        return properties.getProperty("db.password", DEFAULT_DB_PASSWORD);
    }
    
    /**
     * Gets a general property from the configuration.
     * 
     * @param key The property key
     * @param defaultValue The default value if the property is not found
     * @return The property value or the default value if not found
     */
    public static String getProperty(String key, String defaultValue) {
        init();
        return properties.getProperty(key, defaultValue);
    }
}