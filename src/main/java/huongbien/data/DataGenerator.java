package huongbien.data;

import huongbien.entity.*;
import huongbien.jpa.JPAUtil;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.JacksonUtil;
import huongbien.util.Util;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataGenerator {
    private static final EntityManager entityManager = JPAUtil.getEntityManager(PersistenceUnit.MARIADB_JPA_CREATE);
    private static final Faker faker = new Faker();

    private static final List<Cuisine> cuisines = new ArrayList<>();
    private static final List<RestaurantTable> tables = new ArrayList<>();
    private static final List<Employee> employees = new ArrayList<>();
    private static final List<Employee> receptionist = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Promotion> promotions = new ArrayList<>();

    public static void main(String[] args) {
        boolean inserted = true;
        loadTableSampleData(inserted);
        loadCuisineSampleData(inserted);
        loadPromotionSampleData(inserted);
        loadEmployeeSampleData(inserted);
        loadAccountSampleData(inserted);
        loadCustomerSampleData(inserted);

//        for (int i = 0; i < 10; i++) {
//            Order order = DataGenerator.fakeOrder(LocalDate.of(2023, 12, i + 1));
//            System.out.println(order);
//            System.out.println("Order Detail: " + order.getOrderDetails().size());
//        }

//        for (int i = 0; i < 10; i++) {
//            Reservation reservation = DataGenerator.fakeReservation(LocalDate.of(2023, 12, i + 1));
//            System.out.println(reservation);
//            System.out.println("Food Order: " + (reservation.getFoodOrders() == null ? 0 : reservation.getFoodOrders().size()));
//        }

        for (int year = 2024; year <= 2025; year++) {
            for (int month = 1; month <= 12; month++) {
                int maxDay = LocalDate.of(year, month, 1).lengthOfMonth();
                for (int day = 1; day <= maxDay; day++) {
                    LocalDate date = LocalDate.of(year, month, day);

                    int employeeQuantity = faker.number().numberBetween(0, 1);
                    for (int i = 0; i < employeeQuantity; i++) {
                        Employee employee = fakeEmployee(date);
                        employees.add(employee);
                        if (employee.getPosition().equals("Tiếp tân")) {
                            receptionist.add(employee);
                        }
                        if (inserted) {
                            try {
                                entityManager.getTransaction().begin();
                                entityManager.persist(employee);
                                entityManager.getTransaction().commit();
                                System.out.println(employee);
                            } catch (Exception e) {
                                entityManager.getTransaction().rollback();
                            }
                        }
                    }

                    int customerQuantity = faker.number().numberBetween(0, 3);
                    for (int i = 0; i < customerQuantity; i++) {
                        Customer customer = fakeCustomer(date);
                        customers.add(customer);
                        if (inserted) {
                            try {
                                entityManager.getTransaction().begin();
                                entityManager.persist(customer);
                                entityManager.getTransaction().commit();
                                System.out.println(customer);
                            } catch (Exception e) {
                                entityManager.getTransaction().rollback();
                            }
                        }
                    }

                    int reservationQuantity = faker.number().numberBetween(0, 3);
                    for (int i = 0; i < reservationQuantity; i++) {
                        Reservation reservation = fakeReservation(date);
                        if (inserted) {
                            try {
                                entityManager.getTransaction().begin();
                                entityManager.persist(reservation);
                                entityManager.getTransaction().commit();
                            } catch (Exception e) {
                                entityManager.getTransaction().rollback();
                                System.out.println("RESERVATION ERROR: " + reservation);
                                System.out.println(reservation.getTables());
                                e.printStackTrace();
                            }
                        }
                    }

                    int orderQuantity = faker.number().numberBetween(1, 5);
                    for (int i = 0; i < orderQuantity; i++) {
                        Order order = fakeOrder(date);
                        if (inserted) {
                            try {
                                entityManager.getTransaction().begin();
                                entityManager.persist(order);
                                entityManager.getTransaction().commit();
                            } catch (Exception e) {
                                entityManager.getTransaction().rollback();
                                System.out.println("ORDER ERROR: " + order);
                                System.out.println(order.getTables());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static LocalDate fakeDate() {
        return LocalDate.of(faker.number().numberBetween(1990, 2000),
                faker.number().numberBetween(1, 12),
                faker.number().numberBetween(1, 28));
    }

    public static LocalTime fakeTime() {
        return LocalTime.of(faker.number().numberBetween(0, 24),
                faker.number().numberBetween(0, 60),
                faker.number().numberBetween(0, 60));
    }

    public static Employee getRandomEmployee() {
        return receptionist.get(faker.number().numberBetween(0, receptionist.size()));
    }

    public static Customer getRandomCustomer() {
        return customers.get(faker.number().numberBetween(0, customers.size()));
    }

    public static Cuisine getRandomCuisine() {
        return cuisines.get(faker.number().numberBetween(0, cuisines.size()));
    }

    public static Promotion getPromotion(MembershipLevel membershipLevel) {
        for (Promotion promotion : promotions) {
            if (membershipLevel == promotion.getMembershipLevel())
                return promotion;
        }
        return null;
    }

    public static List<RestaurantTable> getRandomTables(int quantity) {
        Set<RestaurantTable> tables = new HashSet<>();
        for (int i = 0; i < quantity; i++) {
            tables.add(DataGenerator.tables.get(faker.number().numberBetween(0, DataGenerator.tables.size())));
        }

        return tables.stream().toList();
    }

    public static void loadCuisineSampleData(boolean inserted) {
        String rootPath = System.getProperty("user.dir");
        String cuisinePath = rootPath + "/src/main/resources/huongbien/data/cuisines.json";
        Cuisine[] cuisineJson = JacksonUtil.readObjectFromFile(cuisinePath, Cuisine[].class);

        assert cuisineJson != null;
        Set<Category> categories = new HashSet<>();
        for (Cuisine cuisine : cuisineJson) {
            String imagePath = String.format("%s/src/main/resources/huongbien/img/foods/%s.jpg", rootPath, cuisine.getId());
            byte[] image = Util.readImage(imagePath);
            if (image == null) {
                System.out.println("Image not found: " + imagePath);
                continue;
            }
            cuisine.setImage(image);
            System.out.println(cuisine);
            cuisines.add(cuisine);
            categories.add(cuisine.getCategory());
        }

        System.out.println(categories);

        if (inserted) {
            for (Category category : categories) {
                entityManager.getTransaction().begin();
                entityManager.persist(category);
                entityManager.getTransaction().commit();
            }

            for (Cuisine cuisine : cuisines) {
                entityManager.getTransaction().begin();
                entityManager.persist(cuisine);
                entityManager.getTransaction().commit();
            }
        }
    }

    public static void loadTableSampleData(boolean inserted) {
        String rootPath = System.getProperty("user.dir");
        String cuisinePath = rootPath + "/src/main/resources/huongbien/data/tables.json";
        RestaurantTable[] tableJson = JacksonUtil.readObjectFromFile(cuisinePath, RestaurantTable[].class);

        assert tableJson != null;
        Set<TableType> tableTypes = new HashSet<>();
        for (RestaurantTable table : tableJson) {
            System.out.println(table);
            tables.add(table);
            tableTypes.add(table.getTableType());
        }

        System.out.println(tableTypes);

        if (inserted) {
            for (TableType tableType : tableTypes) {
                entityManager.getTransaction().begin();
                entityManager.persist(tableType);
                entityManager.getTransaction().commit();
            }

            for (RestaurantTable table : tables) {
                entityManager.getTransaction().begin();
                entityManager.persist(table);
                entityManager.getTransaction().commit();
            }
        }
    }

    public static void loadPromotionSampleData(boolean inserted) {
        String rootPath = System.getProperty("user.dir");
        String cuisinePath = rootPath + "/src/main/resources/huongbien/data/promotions.json";
        Promotion[] promotionJson = JacksonUtil.readObjectFromFile(cuisinePath, Promotion[].class);

        assert promotionJson != null;
        for (Promotion promotion : promotionJson) {
            System.out.println(promotion);
            promotions.add(promotion);

            if (inserted) {
                entityManager.getTransaction().begin();
                entityManager.persist(promotion);
                entityManager.getTransaction().commit();
            }
        }
    }

    public static void loadEmployeeSampleData(boolean inserted) {
        Employee manager = new Employee(
                "NV001122001",
                "Nguyễn Trung Nguyên",
                "12 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh, Viêt Nam",
                "0123456789012",
                "0901234567",
                LocalDate.of(2004, 2, 20),
                Gender.MALE,
                "Đang làm",
                "trungnguyen@example.com",
                LocalDate.of(2021, 2, 12),
                "Quản lý",
                40, 20000, 80000, null, null
        );

        Employee receptionist1 = new Employee(
                "NV001122002",
                "Nguyễn Trần Gia Sĩ",
                "12 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh, Viêt Nam",
                "0123456789015",
                "0901234570",
                LocalDate.of(2004, 2, 20),
                Gender.MALE,
                "Đang làm",
                "nguyensi@example.com",
                LocalDate.of(2021, 2, 12),
                "Tiếp tân",
                40, 20000, 80000, null, null
        );

        Employee receptionist2 = new Employee(
                "NV001122003",
                "Nguyễn Văn Minh",
                "12 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh, Viêt Nam",
                "0123456789016",
                "0901234571",
                LocalDate.of(2004, 2, 20),
                Gender.MALE,
                "Đang làm",
                "nguyenminh@example.com",
                LocalDate.of(2021, 2, 12),
                "Quản lý",
                40, 20000, 80000, null, null
        );

        Employee receptionist3 = new Employee(
                "NV001122004",
                "Trần Di Phong",
                "12 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh, Viêt Nam",
                "0123456789018",
                "0901234573",
                LocalDate.of(2004, 2, 20),
                Gender.MALE,
                "Đang làm",
                "tranphong@example.com",
                LocalDate.of(2021, 2, 12),
                "Quản lý",
                40, 20000, 80000, null, null
        );

        employees.add(manager);
        receptionist.add(receptionist1);
        receptionist.add(receptionist2);
        receptionist.add(receptionist3);

        if (inserted) {
            entityManager.getTransaction().begin();
            entityManager.persist(manager);
            entityManager.persist(receptionist1);
            entityManager.persist(receptionist2);
            entityManager.persist(receptionist3);
            entityManager.getTransaction().commit();
        }
    }

    public static void loadAccountSampleData(boolean inserted) {
//        MK: root
        Account adminAccount = new Account("NV001122001", "4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2", "manager", null, true);

        adminAccount.setEmployee(employees.getFirst());

        if (inserted) {
            entityManager.getTransaction().begin();
            entityManager.persist(adminAccount);
            entityManager.getTransaction().commit();
        }
    }

    public static void loadCustomerSampleData(boolean inserted) {
        for (int i = 0; i < 10; i++) {
            Customer customer = fakeCustomer(fakeDate());
            customers.add(customer);
            if (inserted) {
                entityManager.getTransaction().begin();
                entityManager.persist(customer);
                entityManager.getTransaction().commit();
            }
        }
    }

    public static Employee fakeEmployee(LocalDate hireDate) {
        Employee manager = employees.getFirst();
        String[] positions = {"Nhân viên", "Phục vụ", "Bếp", "Tiếp tân", "Bảo vệ"};

        Employee employee = new Employee();
        employee.setId(Employee.generateId(hireDate));
        employee.setName(faker.name().fullName());
        employee.setAddress(faker.address().fullAddress());
        employee.setCitizenId(faker.idNumber().valid());
        employee.setPhoneNumber(faker.phoneNumber().cellPhone());
        employee.setBirthday(fakeDate());
        employee.setGender(Gender.values()[faker.number().numberBetween(0, 2)]);
        employee.setStatus("Đang làm");
        employee.setEmail(faker.internet().emailAddress());
        employee.setHireDate(hireDate);
        employee.setPosition(positions[faker.number().numberBetween(0, 4)]);
        employee.setWorkHours(faker.number().randomDouble(2, 20, 40));
        employee.setHourlyPay(faker.number().randomDouble(2, 10000, 20000));
        employee.setSalary(employee.getWorkHours() * employee.getHourlyPay());
        employee.setProfileImage(null);
        employee.setManager(manager);

        return employee;
    }

    public static Customer fakeCustomer(LocalDate registrationDate) {
        Customer customer = new Customer();
        customer.setId(Customer.generateId(registrationDate));
        customer.setName(faker.name().fullName());
        customer.setAddress(faker.address().fullAddress());
        customer.setGender(Gender.values()[faker.number().numberBetween(0, 2)]);
        customer.setPhoneNumber(faker.phoneNumber().cellPhone());
        customer.setEmail(faker.internet().emailAddress());
        customer.setBirthday(fakeDate());
        customer.setRegistrationDate(registrationDate);
        customer.setAccumulatedPoints(faker.number().numberBetween(0, 1000));
        customer.setMembershipLevel(MembershipLevel.values()[faker.number().numberBetween(0, 4)]);
        return customer;
    }

    public static Payment fakePayment(double amount, LocalDate paymentDate, LocalTime paymentTime) {
        return new Payment(Payment.generateId(paymentDate, paymentTime) , amount, paymentDate, paymentTime, PaymentMethod.values()[Util.randomNumber(0, 1)]);
    }

    public static List<FoodOrder> fakeFoodOrders(Reservation reservation) {
        int quantity = faker.number().numberBetween(0, 5);
        Set<FoodOrder> foodOrders = new HashSet<>();
        for (int i = 0; i < quantity; i++) {
            FoodOrder foodOrder = new FoodOrder();
            foodOrder.setId(FoodOrder.generateId(reservation.getId()));
            foodOrder.setQuantity(faker.number().numberBetween(1, 5));
            foodOrder.setCuisine(getRandomCuisine());
            foodOrder.setNote(faker.lorem().sentence());
            foodOrder.setSalePrice(foodOrder.getQuantity() * foodOrder.getCuisine().getPrice());
            foodOrders.add(foodOrder);
        }

        return foodOrders.stream().toList();
    }

    public static Reservation fakeReservation(LocalDate reservationDate) {
        String[] partyTypes = {"Tiệc sinh nhật", "Tiệc gia đình", "Gặp mặt bạn bè", "Tiệc công ty", "Tiệc tất niên", "Ăn bình thường"};
        Reservation reservation = new Reservation();
        reservation.setReservationDate(reservationDate);
        reservation.setReservationTime(fakeTime());
        reservation.setId(Reservation.generateId(reservation.getReservationDate(), reservation.getReservationTime()));
        reservation.setReceiveDate(reservationDate.plusDays(faker.number().numberBetween(1, 5)));
        reservation.setReceiveTime(fakeTime());
        reservation.setPartySize(faker.number().numberBetween(2, 16));
        reservation.setPartyType(partyTypes[faker.number().numberBetween(0, partyTypes.length)]);
        reservation.setStatus(ReservationStatus.values()[faker.number().numberBetween(0, ReservationStatus.values().length)]);
        reservation.setEmployee(getRandomEmployee());
        reservation.setCustomer(getRandomCustomer());
        reservation.setTables(getRandomTables(faker.number().numberBetween(1, 3)));

        List<FoodOrder> foodOrders = fakeFoodOrders(reservation);
        reservation.setFoodOrders(foodOrders.isEmpty() ? null : foodOrders);

        int vipTables = (int) reservation.getTables().stream()
                .filter(table -> table.getTableType().getName().equals("Bàn VIP"))
                .count();
        reservation.setDeposit(vipTables > 0 ? 100_000 * vipTables : 0);
        reservation.setRefundDeposit(
                reservation.getStatus().equals(ReservationStatus.CANCELLED)
                        ? reservation.getDeposit() * 0.5
                        : 0
        );

        Payment payment = fakePayment(reservation.getDeposit(), reservation.getReservationDate(), reservation.getReservationTime());
        payment.setReservation(reservation);
        reservation.setPayment(
                reservation.getDeposit() > 0
                        ? payment
                        : null
        );
        return reservation;
    }

    public static List<OrderDetail> fakeOrderDetails(Order order) {
        int orderDetailQuantity = faker.number().numberBetween(2, 5);
        Set<OrderDetail> orderDetails = new HashSet<>();

        for (int i = 0; i < orderDetailQuantity; i++) {
            Cuisine cuisine = getRandomCuisine();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(OrderDetail.generateId(order.getId()));
            orderDetail.setQuantity(faker.number().numberBetween(1, 3));
            orderDetail.setNote("");
            orderDetail.setSalePrice(orderDetail.getQuantity() * cuisine.getPrice());
            orderDetail.setCuisine(cuisine);
            orderDetails.add(orderDetail);
        }

        return orderDetails.stream().toList();
    }

    public static Order fakeOrder(LocalDate orderDate) {
        Order order = new Order();

        order.setNotes("");
        order.setOrderDate(orderDate);
        order.setOrderTime(fakeTime());
        order.setId(Order.generateId(orderDate, order.getOrderTime()));
        order.setTables(getRandomTables(faker.number().numberBetween(1, 1)));
        order.setCustomer(getRandomCustomer());
        order.setEmployee(getRandomEmployee());
        order.setOrderDetails(fakeOrderDetails(order));

        order.setPromotion(getPromotion(order.getCustomer().getMembershipLevel()));
        order.setDiscount(order.getPromotion() == null ? 0 : order.getPromotion().getDiscount());
        order.setTotalAmount(order.calculateGrandTotal());
        order.setPaymentAmount(Util.suggestMoneyReceived((int) order.getTotalAmount())[0]);
        order.setDispensedAmount(order.getPaymentAmount() - order.getTotalAmount());
        order.setPayment(fakePayment(order.getPaymentAmount(), orderDate, fakeTime()));

        return order;
    }
}
