package huongbien.rmi;

import huongbien.dao.impl.*;
import huongbien.dao.remote.*;

import javax.naming.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer {
    private static final Logger LOGGER = Logger.getLogger(RMIServer.class.getName());
    private static final int RMI_PORT = 2975; // Changed to 2975 as specified
    private static final String RMI_HOST = "localhost"; // Using localhost as specified

    private Context context;
    private Registry registry;

    public static void main(String[] args) {
        RMIServer server = new RMIServer();
        server.start();

        server.listBoundServices(); // List all services after starting the server

        // Keep the server running
        while (true) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                server.stop();
                break;
            }
        }
    }

    public void start() {
        try {
            LOGGER.info("Starting JNDI RMI server on port " + RMI_PORT);

            // Create or get registry
            try {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                LOGGER.info("Created new RMI registry on port " + RMI_PORT);
            } catch (RemoteException e) {
                LOGGER.info("Registry already exists, getting reference");
                registry = LocateRegistry.getRegistry(RMI_PORT);
            }

            // Set up JNDI environment
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://" + RMI_HOST + ":" + RMI_PORT);

            context = new InitialContext(env);

            // Create DAOs and service wrappers, then bind them to JNDI
            bindServices();

            LOGGER.info("JNDI RMI server started successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting JNDI RMI server", e);
        }
    }

    private void bindServices() throws RemoteException, NamingException {
        // Create all DAO objects
        IReservationDAO reservationDAO = new ReservationDAO();
        IAccountDAO accountDAO = new AccountDAO();
        ICategoryDAO categoryDAO = new CategoryDAO();
        ICuisineDAO cuisineDAO = new CuisineDAO();
        IEmployeeDAO employeeDAO = new EmployeeDAO();
        IFoodOrderDAO foodOrderDAO = new FoodOrderDAO();
        ICustomerDAO customerDAO = new CustomerDAO();
        IOrderDAO orderDAO = new OrderDAO();
        IOrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        IPaymentDAO paymentDAO = new PaymentDAO();
        IPromotionDAO promotionDAO = new PromotionDAO();
        IStatisticsDAO statisticsDAO = new StatisticsDAO();
        ITableDAO tableDAO = new TableDAO();
        ITableTypeDAO tableTypeDAO = new TableTypeDAO();

        // Bind DAOs to JNDI context
        bindService("reservationDAO", reservationDAO);
        bindService("accountDAO", accountDAO);
        bindService("categoryDAO", categoryDAO);
        bindService("cuisineDAO", cuisineDAO);
        bindService("employeeDAO", employeeDAO);
        bindService("foodOrderDAO", foodOrderDAO);
        bindService("customerDAO", customerDAO);
        bindService("orderDAO", orderDAO);
        bindService("orderDetailDAO", orderDetailDAO);
        bindService("paymentDAO", paymentDAO);
        bindService("promotionDAO", promotionDAO);
        bindService("statisticsDAO", statisticsDAO);
        bindService("tableDAO", tableDAO);
        bindService("tableTypeDAO", tableTypeDAO);
    }

    private void bindService(String name, Object service) {
        try {
            String jndiUrl = "rmi://" + RMI_HOST + ":" + RMI_PORT + "/" + name;
            context.bind(jndiUrl, service);
            LOGGER.info(name + " bound to JNDI at " + jndiUrl);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to bind " + name, e);
        }
    }

    public void stop() {
        try {
            // Unbind all services
            String[] serviceNames = {
                    "reservationDAO", "accountDAO", "categoryDAO", "cuisineDAO",
                    "employeeDAO", "foodOrderDAO", "customerDAO", "orderDAO",
                    "orderDetailDAO", "paymentDAO", "promotionDAO", "statisticsDAO",
                    "tableDAO", "tableTypeDAO"
            };

            for (String name : serviceNames) {
                try {
                    String jndiUrl = "rmi://" + RMI_HOST + ":" + RMI_PORT + "/" + name;
                    context.unbind(jndiUrl);
                    LOGGER.info("Unbound service: " + jndiUrl);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error unbinding " + name, e);
                }
            }
            LOGGER.info("All services unbound, JNDI RMI server stopped");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error stopping JNDI RMI server", e);
        }
    }

    /**
     * Lists all services currently bound in the JNDI context
     */
    public void listBoundServices() {
        System.out.println("\n========================================");
        System.out.println("RMI Services available at rmi://" + RMI_HOST + ":" + RMI_PORT);
        System.out.println("----------------------------------------");
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://" + RMI_HOST + ":" + RMI_PORT);

            Context listContext = new InitialContext(env);
            NamingEnumeration<NameClassPair> services = listContext.list("");

            if (!services.hasMoreElements()) {
                System.out.println("No services bound in this context");
            }

            while (services.hasMoreElements()) {
                NameClassPair service = services.nextElement();
                System.out.println("• " + service.getName());
            }

        } catch (Exception e) {
            System.out.println("Error listing services: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error listing bound services", e);
        }
        System.out.println("========================================\n");
    }
}