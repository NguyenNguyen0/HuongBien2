package huongbien.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class for the HuongBien server.
 * Loads server settings from a properties file.
 */
public class ServerConfig {
    private static final String DEFAULT_CONFIG_PATH = "server.properties";
    private static Properties properties = new Properties();
    
    // Default server settings
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final String DEFAULT_RMI_HOST = "localhost";
    
    static {
        loadConfig();
    }
    
    /**
     * Load the server configuration from the default properties file.
     */
    public static void loadConfig() {
        loadConfig(DEFAULT_CONFIG_PATH);
    }
    
    /**
     * Load the server configuration from a specific properties file.
     * 
     * @param configPath Path to the properties file.
     */
    public static void loadConfig(String configPath) {
        try {
            properties.load(new FileInputStream(configPath));
            System.out.println("Successfully loaded server configuration from " + configPath);
        } catch (IOException e) {
            System.err.println("Warning: Could not load server configuration file: " + e.getMessage());
            System.out.println("Using default server configuration");
        }
    }
    
    /**
     * Get the RMI port for the server.
     * 
     * @return The configured RMI port, or the default if not specified.
     */
    public static int getRmiPort() {
        return Integer.parseInt(properties.getProperty("rmi.port", String.valueOf(DEFAULT_RMI_PORT)));
    }
    
    /**
     * Get the RMI host for the server.
     * 
     * @return The configured RMI host, or the default if not specified.
     */
    public static String getRmiHost() {
        return properties.getProperty("rmi.host", DEFAULT_RMI_HOST);
    }
}