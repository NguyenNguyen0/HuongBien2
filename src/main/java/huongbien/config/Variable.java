package huongbien.config;

import huongbien.dao.TableDAO;
import huongbien.jpa.PersistenceUnit;

public class Variable {
    //OrderCuisineController.java
    //---
    public static String category = "Loại món";

    //OrderTableController
    //--
//    public static int floor = (new TableDAO(PersistenceUnit.MARIADB_JPA)).getTopFloor().getFloor();
    public static int floor = 3;
    public static String status = "Trạng thái";
    public static String tableTypeName = "Loại bàn";
    public static String seats = "Số chỗ";
    //---
    public static String tableVipID = "LB002";
    public static double tableVipPrice = 100000;
    //---
    public static String[] partyTypesArray = {"Gia đình", "Sinh nhật", "Đám cưới", "Đám dỗ", "Đám hỏi", "Kỷ niệm", "Hội nghị", "Hẹn hò", "Khác..."};

    //EmployeeManagementController.java
    //---
    public static String[] statusOptions = {"Đang làm", "Nghỉ phép", "Nghỉ việc"};
    public static String[] searchMethods = {"Còn làm việc", "Tất cả", "Tìm theo tên", "Tìm theo số điện thoại", "Tìm theo chức vụ"};
    public static String addMode = "Thêm";
    public static String editMode = "Sửa";

    //ReservationManagementController.java
    //---
    public static String[] statusReservation = {"Chưa nhận", "Hoàn thành", "Đã hủy"};

    //PreOrderController.java
    //---
    public static String[] paymentMethods = {"Tiền mặt", "Chuyển khoản"};

    //Password WIFI
    public static String PASSWORD_WIFI = "12345678";
}
