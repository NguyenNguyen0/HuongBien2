package huongbien.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Client configuration manager for HuongBien Restaurant Management System.
 * Handles loading and accessing client configuration properties.
 */
public class ClientConfig {
    private static final Properties properties = new Properties();
    private static boolean isInitialized = false;
    
    // Default values
    private static final String DEFAULT_RMI_HOST = "localhost";
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    private static final int DEFAULT_CONNECTION_RETRIES = 3;
    
    /**
     * Loads the client configuration properties from the client.properties file.
     */
    public static void init() {
        if (isInitialized) {
            return;
        }
        
        try (InputStream is = ClientConfig.class.getClassLoader().getResourceAsStream("client.properties")) {
            if (is != null) {
                properties.load(is);
                System.out.println("Client configuration loaded successfully");
            } else {
                System.out.println("client.properties not found, using default values");
            }
        } catch (IOException e) {
            System.err.println("Error loading client configuration: " + e.getMessage());
            e.printStackTrace();
        }
        
        isInitialized = true;
    }
    
    /**
     * Gets the RMI host from the configuration.
     * 
     * @return The configured RMI host or the default value if not specified
     */
    public static String getRmiHost() {
        init();
        return properties.getProperty("rmi.host", DEFAULT_RMI_HOST);
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
     * Gets the connection timeout from the configuration.
     * 
     * @return The configured connection timeout or the default value if not specified
     */
    public static int getConnectionTimeout() {
        init();
        return Integer.parseInt(properties.getProperty("connection.timeout", 
                               String.valueOf(DEFAULT_CONNECTION_TIMEOUT)));
    }
    
    /**
     * Gets the connection retry count from the configuration.
     * 
     * @return The configured connection retry count or the default value if not specified
     */
    public static int getConnectionRetries() {
        init();
        return Integer.parseInt(properties.getProperty("connection.retries", 
                               String.valueOf(DEFAULT_CONNECTION_RETRIES)));
    }
    
    /**
     * Gets the UI theme from the configuration.
     * 
     * @return The configured UI theme or "light" if not specified
     */
    public static String getUiTheme() {
        init();
        return properties.getProperty("ui.theme", "light");
    }
    
    /**
     * Gets the UI locale from the configuration.
     * 
     * @return The configured UI locale or "vi_VN" if not specified
     */
    public static String getUiLocale() {
        init();
        return properties.getProperty("ui.locale", "vi_VN");
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