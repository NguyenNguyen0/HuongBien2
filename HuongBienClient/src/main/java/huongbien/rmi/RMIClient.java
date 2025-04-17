package huongbien.rmi;

import huongbien.dao.remote.*;
import lombok.Getter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIClient {
    private static final Logger LOGGER = Logger.getLogger(RMIClient.class.getName());
    private static final String SERVER_HOST = "localhost";
    private static final int RMI_PORT = 2975;

    // Singleton instance
    private static volatile RMIClient instance;

    private static Registry registry;
    /**
     * -- GETTER --
     * Checks if client is connected to the RMI server
     *
     * @return true if connected, false otherwise
     */
    @Getter
    private static boolean connected = false;

    // Private constructor to prevent instantiation
    private RMIClient() {
        connect();
    }

    /**
     * Gets the singleton instance of RMIClient
     *
     * @return The singleton instance
     */
    public static RMIClient getInstance() {
        // Double-checked locking for thread safety
        if (instance == null) {
            synchronized (RMIClient.class) {
                instance = new RMIClient();
            }
        }
        return instance;
    }

    private static boolean connect() {
        try {
            LOGGER.info("Connecting to RMI server at " + SERVER_HOST + ":" + RMI_PORT);
            registry = LocateRegistry.getRegistry(SERVER_HOST, RMI_PORT);

            // Simple check to verify connection - list available services
            String[] services = registry.list();
            LOGGER.info("Available services: " + String.join(", ", services));

            connected = true;
            return true;
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to RMI server", e);
            connected = false;
            return false;
        }
    }

    public static void reset() {
        if (instance != null) {
            instance.disconnect();
        }
        instance = null;
    }

    public static void main(String[] args) {
        try {
            // Get the singleton instance
            RMIClient client = RMIClient.getInstance();

            // List available services
            String[] services = registry.list();
            LOGGER.info("Available services: " + String.join(", ", services));
            LOGGER.info("Available services: " + String.join(", ", services));

            // Check if tableTypeDAO is in the list
            boolean tableTypeDAOFound = false;
            for (String service : services) {
                if (service.equals("tableTypeDAO")) {
                    tableTypeDAOFound = true;
                    break;
                }
            }

            LOGGER.info("tableTypeDAO service found: " + tableTypeDAOFound);

            // Only try to use it if found
            if (tableTypeDAOFound) {
                ITableTypeDAO tableTypeDAO = client.getTableTypeDAO();
                LOGGER.info("Found " + tableTypeDAO.getAll().size() + " table types");
            }

            // Disconnect when done
            client.disconnect();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in main", e);
        }
    }

    public boolean reconnect() {
        connected = false;
        return connect();
    }

    /**
     * Gets a remote ReservationDAO object
     *
     * @return The remote ReservationDAO
     */
    public IReservationDAO getReservationDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IReservationDAO) registry.lookup("reservationDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get ReservationDAO", e);
            throw e;
        }
    }

    public IAccountDAO getAccountDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IAccountDAO) registry.lookup("accountDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get AccountDAO", e);
            throw e;
        }
    }

    public ICategoryDAO getCategoryDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (ICategoryDAO) registry.lookup("categoryDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get CategoryDAO", e);
            throw e;
        }
    }

    public ICuisineDAO getCuisineDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (ICuisineDAO) registry.lookup("cuisineDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get CuisineDAO", e);
            throw e;
        }
    }

    public IEmployeeDAO getEmployeeDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IEmployeeDAO) registry.lookup("employeeDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get EmployeeDAO", e);
            throw e;
        }
    }

    public IFoodOrderDAO getFoodOrderDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IFoodOrderDAO) registry.lookup("foodOrderDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get FoodOrderDAO", e);
            throw e;
        }
    }

    public ICustomerDAO getCustomerDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (ICustomerDAO) registry.lookup("customerDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get CustomerDAO", e);
            throw e;
        }
    }

    public IOrderDAO getOrderDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IOrderDAO) registry.lookup("orderDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get OrderDAO", e);
            throw e;
        }
    }

    public IOrderDetailDAO getOrderDetailDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IOrderDetailDAO) registry.lookup("orderDetailDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get OrderDetailDAO", e);
            throw e;
        }
    }

    /**
     * Gets a remote PaymentDAO object
     *
     * @return The remote PaymentDAO
     */
    public IPaymentDAO getPaymentDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IPaymentDAO) registry.lookup("paymentDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get PaymentDAO", e);
            throw e;
        }
    }

    /**
     * Gets a remote PromotionDAO object
     *
     * @return The remote PromotionDAO
     */
    public IPromotionDAO getPromotionDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IPromotionDAO) registry.lookup("promotionDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get PromotionDAO", e);
            throw e;
        }
    }

    /**
     * Gets a remote StatisticsDAO object
     *
     * @return The remote StatisticsDAO
     */
    public IStatisticsDAO getStatisticsDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (IStatisticsDAO) registry.lookup("statisticsDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get statisticsDAO", e);
            throw e;
        }
    }

    /**
     * Gets a remote TableDAO object
     *
     * @return The remote TableDAO
     */
    public ITableDAO getTableDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (ITableDAO) registry.lookup("tableDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get TableDAO", e);
            throw e;
        }
    }

    /**
     * Gets a remote TableTypeDAO object
     *
     * @return The remote TableTypeDAO
     */
    public ITableTypeDAO getTableTypeDAO() throws RemoteException, NotBoundException {
        ensureConnected();

        try {
            return (ITableTypeDAO) registry.lookup("tableTypeDAO");
        } catch (RemoteException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to get TableTypeDAO", e);
            throw e;
        }
    }

    /**
     * Ensures the client is connected to the RMI server
     *
     * @throws IllegalStateException if not connected and reconnection fails
     */
    private void ensureConnected() {
        if (!connected && !connect()) {
            throw new IllegalStateException("Not connected to RMI server and reconnection failed");
        }
    }

    public void disconnect() {
        registry = null;
        connected = false;
        LOGGER.info("Disconnected from RMI server");
    }
}