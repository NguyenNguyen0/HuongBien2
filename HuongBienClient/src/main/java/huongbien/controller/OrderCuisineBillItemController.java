package huongbien.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.Constants;
import huongbien.dao.remote.ICuisineDAO;
import huongbien.entity.OrderDetail;
import huongbien.rmi.RMIClient;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;

public class OrderCuisineBillItemController {
    //Controller area
    public OrderCuisineController orderCuisineController;
    @FXML
    private Label cuisineIdLabel;
    @FXML
    private Label cuisineNameLabel;
    @FXML
    private Label cuisineNoteLabel;
    @FXML
    private Label cuisineQuantityLabel;
    @FXML
    private Label cuisineSalePriceLabel;
    @FXML
    private Label cuisineTotalPriceLabel;

    public void setOrderBillController(OrderCuisineController orderCuisineController) {
        this.orderCuisineController = orderCuisineController;
    }

    public void setDataBill(OrderDetail orderDetail) {
        cuisineIdLabel.setText(orderDetail.getCuisine().getId());
        cuisineNameLabel.setText(orderDetail.getCuisine().getName());
        cuisineSalePriceLabel.setText(String.format("%,.0f VNĐ", orderDetail.getCuisine().getPrice()));
        cuisineNoteLabel.setText(orderDetail.getNote());
        cuisineQuantityLabel.setText(orderDetail.getQuantity() + "");
        cuisineTotalPriceLabel.setText(String.format("%,.0f VNĐ", orderDetail.getSalePrice()));
    }

    private void removeFromJson(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to remove item.");
            return;
        }
        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();
            if (existingCuisineID.equals(cuisineID)) {
                try {
                    ICuisineDAO cuisineDAO = RMIClient.getInstance().getCuisineDAO();
                    jsonArray.remove(i);
                    ToastsMessage.showMessage("Đã xoá món" + cuisineDAO.getById(cuisineID).getName() + " ra khỏi danh sách", "success");
                    break;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.CUISINE_PATH);
    }

    private void increaseQuantityInJSON(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to increase quantity.");
            return;
        }

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

            if (existingCuisineID.equals(cuisineID)) {
                int currentQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
                double price = jsonObject.get("Cuisine Price").getAsDouble();

                int newQuantity = currentQuantity + 1;
                jsonObject.addProperty("Cuisine Quantity", newQuantity);

                double newMoney = price * newQuantity;
                jsonObject.addProperty("Cuisine Money", newMoney);

                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.CUISINE_PATH);
    }

    private void decreaseQuantityInJSON(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH); // Đọc file JSON
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to decrease quantity.");
            return;
        }

        try {
            ICuisineDAO cuisineDAO = RMIClient.getInstance().getCuisineDAO();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

                if (existingCuisineID.equals(cuisineID)) {
                    int currentQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
                    double price = jsonObject.get("Cuisine Price").getAsDouble();

                    if (currentQuantity > 1) {
                        int newQuantity = currentQuantity - 1;
                        jsonObject.addProperty("Cuisine Quantity", newQuantity);

                        double newMoney = price * newQuantity;
                        jsonObject.addProperty("Cuisine Money", newMoney);
                    } else {
                        jsonArray.remove(i);
                        ToastsMessage.showMessage("Đã xoá món: " + cuisineDAO.getById(cuisineID).getName() + " ra khỏi danh sách", "success");
                    }
                    break;
                }
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        Utils.writeJsonToFile(jsonArray, Constants.CUISINE_PATH);
    }

    private void updateNoteInJSON(String cuisineID, String newNote) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH); // Đọc file JSON
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to update note.");
            return;
        }
        try {
            ICuisineDAO cuisineDAO = RMIClient.getInstance().getCuisineDAO();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

                if (existingCuisineID.equals(cuisineID)) {
                    jsonObject.addProperty("Cuisine Note", newNote);
                    ToastsMessage.showMessage("Đã cập nhật ghi chú cho món: " + cuisineDAO.getById(cuisineID).getName() + " với nội dung là: " + newNote, "success");
                    break;
                }
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        Utils.writeJsonToFile(jsonArray, Constants.CUISINE_PATH);
    }


    @FXML
    void onDecreaseCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        decreaseQuantityInJSON(cuisineID);
        orderCuisineController.billGridPane.getChildren().clear();
        orderCuisineController.loadBill();
        //update lbl
        orderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onDeleteCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        removeFromJson(cuisineID);
        orderCuisineController.billGridPane.getChildren().clear();
        orderCuisineController.loadBill();
        //update lbl
        orderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onIncreaseCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        increaseQuantityInJSON(cuisineID);
        orderCuisineController.billGridPane.getChildren().clear();
        orderCuisineController.loadBill();
        //update lbl
        orderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onNoteCuisineButtonClicked(ActionEvent event) throws FileNotFoundException {
        // Hiển thị dialog để người dùng nhập ghi chú mới
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Nhập ghi chú cho món ăn:");
        inputDialog.setContentText("Ghi chú:");
        inputDialog.initStyle(StageStyle.UNDECORATED);
        // Lấy giá trị ghi chú từ dialog nếu người dùng nhấn OK
        Optional<String> result = inputDialog.showAndWait();
        result.ifPresent(newNote -> {
            String cuisineID = cuisineIdLabel.getText();
            updateNoteInJSON(cuisineID, newNote);
            orderCuisineController.billGridPane.getChildren().clear();
            try {
                orderCuisineController.loadBill();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
