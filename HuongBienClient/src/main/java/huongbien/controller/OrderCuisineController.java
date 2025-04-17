package huongbien.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.remote.ICategoryDAO;
import huongbien.dao.remote.ICuisineDAO;
import huongbien.dao.remote.ITableDAO;
import huongbien.entity.Category;
import huongbien.entity.Cuisine;
import huongbien.entity.OrderDetail;
import huongbien.entity.Table;
import huongbien.rmi.RMIClient;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderCuisineController implements Initializable {
    @FXML
    public GridPane billGridPane;
    @FXML
    public TextField cuisineNameTextField;
    @FXML
    public ComboBox<Pair<String, String>> categoryComboBox;
    //Controller area
    public RestaurantMainManagerController restaurantMainManagerController;
    public RestaurantMainStaffController restaurantMainStaffController;
    @FXML
    private ScrollPane cuisineScrollPane;
    @FXML
    private GridPane cuisineGridPane;
    @FXML
    private ScrollPane billScrollPane;
    @FXML
    private Label tableInfoLabel;
    @FXML
    private Label cuisineAmountLabel;
    @FXML
    private Label cuisineQuantityLabel;
    @FXML
    private Label tableAmountLabel;
    @FXML
    private Label totalAmountLabel;

    public void setRestaurantMainManagerController(RestaurantMainManagerController restaurantMainManagerController) {
        this.restaurantMainManagerController = restaurantMainManagerController;
    }

    public void setRestaurantMainStaffController(RestaurantMainStaffController restaurantMainStaffController) {
        this.restaurantMainStaffController = restaurantMainStaffController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCategoryComboBox();
        loadCuisine("", "");
        try {
            loadBill();
            setCuisinesInfoFromJSON();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCategoryComboBox() {
        try {
            ICategoryDAO categoryDAO = RMIClient.getInstance().getCategoryDAO();
            List<Category> categories = categoryDAO.getAll();
            ObservableList<Pair<String, String>> categoryItems = FXCollections.observableArrayList();
            categoryItems.add(new Pair<>("-1", "Loại món"));
            for (Category category : categories) {
                categoryItems.add(new Pair<>(String.valueOf(category.getId()), category.getName()));
            }
            categoryComboBox.setItems(categoryItems);
            categoryComboBox.setConverter(new StringConverter<Pair<String, String>>() {
                @Override
                public String toString(Pair<String, String> pair) {
                    return pair.getValue();
                }

                @Override
                public Pair<String, String> fromString(String string) {
                    return null;
                }
            });
            categoryComboBox.getSelectionModel().selectFirst();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCuisine(String cuisineName, String category) {
        List<Cuisine> cuisines = new ArrayList<>(getCuisineData(cuisineName, category));
        int columns = 0;
        int rows = 1;
        try {
            for (Cuisine cuisine : cuisines) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/huongbien/fxml/OrderCuisineItem.fxml"));
                VBox cuisineBox = fxmlLoader.load();
                OrderCuisineItemController orderCuisineItemController = fxmlLoader.getController();
                orderCuisineItemController.setCuisineData(cuisine);
                orderCuisineItemController.setOrderCuisineController(this);
                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }
                cuisineGridPane.add(cuisineBox, columns++, rows);
                GridPane.setMargin(cuisineBox, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cuisineScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cuisineGridPane.prefWidthProperty().bind(cuisineScrollPane.widthProperty());
    }

    public void loadBill() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getBillData());
        int columns = 0;
        int rows = 1;
        try {
            for (OrderDetail orderDetail : orderDetails) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/huongbien/fxml/OrderCuisineBillItem.fxml"));
                HBox billBox = fxmlLoader.load();
                OrderCuisineBillItemController orderCuisineBillItemController = fxmlLoader.getController();
                orderCuisineBillItemController.setDataBill(orderDetail);
                orderCuisineBillItemController.setOrderBillController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                billGridPane.add(billBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        billScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        billGridPane.prefWidthProperty().bind(billScrollPane.widthProperty());
    }

    public List<OrderDetail> readFromBillJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String id = jsonObject.get("Cuisine ID").getAsString();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            //set item cuisine bill
            Cuisine cuisine = new Cuisine();
            cuisine.setId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<Cuisine> getCuisineData(String cuisineName, String category) {
        try {
            ICuisineDAO cuisineDAO = RMIClient.getInstance().getCuisineDAO();
            return cuisineDAO.getLookUpCuisine(cuisineName, category);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<OrderDetail> getBillData() throws FileNotFoundException {
        return readFromBillJSON();
    }

    public void setCuisinesInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);

        int totalQuantityCuisine = 0;
        double cuisineAmount = 0.0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            int cuisineQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalQuantityCuisine += cuisineQuantity;
            cuisineAmount += cuisineMoney;
        }
        //table
        double tableAmount = 0.0;
        StringBuilder tableInfoBuilder = new StringBuilder();
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            try {
                ITableDAO tableDAO = RMIClient.getInstance().getTableDAO();
                Table table = tableDAO.getById(id);
                if (table != null) {
                    //set table text
                    String floorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                    tableInfoBuilder.append(table.getName()).append(" (").append(floorStr).append("), ");
                    //calculator table amount
                    tableAmount += table.getTableType().getId().equals(Variable.tableVipID) ? Variable.tableVipPrice : 0;
                } else {
                    tableInfoBuilder.append("Thông tin bàn không xác định, ");
                }
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (!tableInfoBuilder.isEmpty()) {
            tableInfoBuilder.setLength(tableInfoBuilder.length() - 2);
        }
        //setLabel
        tableInfoLabel.setText(tableInfoBuilder.toString());
        cuisineQuantityLabel.setText(totalQuantityCuisine + " món");
        cuisineAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount));
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
        totalAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount + tableAmount));
    }

    @FXML
    void onOrderPaymentButtonAction(ActionEvent event) throws IOException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        if (!jsonArray.isEmpty()) {
            if (restaurantMainManagerController != null) {
                restaurantMainManagerController.openOrderPayment();
            } else {
                restaurantMainStaffController.openOrderPayment();
            }
        } else {
            ToastsMessage.showMessage("Vui lòng chọn món ăn trước khi thanh toán!", "warning");
        }
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        if (restaurantMainManagerController != null) {
            restaurantMainManagerController.openOrderTable();
        } else {
            restaurantMainStaffController.openOrderTable();
        }
    }

    @FXML
    void onClearCuisineButtonAction(ActionEvent event) throws FileNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Thông báo");
        alert.setContentText("Bạn có chắc chắn muốn xóa tất cả món ăn đã chọn?");
        ButtonType btn_ok = new ButtonType("Xoá tất cả");
        ButtonType btn_cancel = new ButtonType("Không");
        alert.getButtonTypes().setAll(btn_ok, btn_cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
            billGridPane.getChildren().clear();
            loadBill();
        }
        ToastsMessage.showMessage("Đã xóa tất cả món ăn ra khỏi danh sách!", "success");
    }

    public void filterFoodHandler() {
        String cuisineName = cuisineNameTextField.getText();
        Pair<String, String> selectedCategory = categoryComboBox.getValue();
        String categoryId = selectedCategory != null ? selectedCategory.getKey() : "";
        cuisineGridPane.getChildren().clear();
        loadCuisine(cuisineName, categoryId.equals("-1") ? "" : categoryId);
    }

    @FXML
    void cuisineNameTextField(KeyEvent event) {
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> filterFoodHandler());
        pause.play();
    }

    @FXML
    void categoryComboBox(ActionEvent event) {
        filterFoodHandler();
    }

    @FXML
    void onCuisineNameTextFieldButton(ActionEvent event) {
        cuisineNameTextField.setText("");
        filterFoodHandler();
    }
}
