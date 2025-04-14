package huongbien.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class for the HuongBien client.
 * Loads client settings from a properties file.
 */
public class ClientConfig {
    private static final String DEFAULT_CONFIG_PATH = "client.properties";
    private static Properties properties = new Properties();
    
    // Default client settings
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final String DEFAULT_RMI_HOST = "localhost";
    
    static {
        loadConfig();
    }
    
    /**
     * Load the client configuration from the default properties file.
     */
    public static void loadConfig() {
        loadConfig(DEFAULT_CONFIG_PATH);
    }
    
    /**
     * Load the client configuration from a specific properties file.
     * 
     * @param configPath Path to the properties file.
     */
    public static void loadConfig(String configPath) {
        try {
            properties.load(new FileInputStream(configPath));
            System.out.println("Successfully loaded client configuration from " + configPath);
        } catch (IOException e) {
            System.err.println("Warning: Could not load client configuration file: " + e.getMessage());
            System.out.println("Using default client configuration");
        }
    }
    
    /**
     * Get the RMI port for connecting to the server.
     * 
     * @return The configured RMI port, or the default if not specified.
     */
    public static int getRmiPort() {
        return Integer.parseInt(properties.getProperty("rmi.port", String.valueOf(DEFAULT_RMI_PORT)));
    }
    
    /**
     * Get the RMI host for connecting to the server.
     * 
     * @return The configured RMI host, or the default if not specified.
     */
    public static String getRmiHost() {
        return properties.getProperty("rmi.host", DEFAULT_RMI_HOST);
    }
    
    /**
     * Get the application UI theme.
     * 
     * @return The configured UI theme.
     */
    public static String getUiTheme() {
        return properties.getProperty("ui.theme", "light");
    }
    
    /**
     * Get the locale for the application.
     * 
     * @return The configured locale.
     */
    public static String getLocale() {
        return properties.getProperty("ui.locale", "vi_VN");
    }
}