package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.Constants;
import huongbien.dao.CuisineDAO;
import huongbien.entity.OrderDetail;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Optional;

public class PreOrderCuisineBillItemController {
    //Controller area
    public PreOrderCuisineController preOrderCuisineController;
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

    public void setPreOrderBillController(PreOrderCuisineController preOrderCuisineController) {
        this.preOrderCuisineController = preOrderCuisineController;
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
                jsonArray.remove(i);
                ToastsMessage.showMessage("Đã xoá món" + (new CuisineDAO()).getById(cuisineID).getName() + " ra khỏi danh sách", "success");
                break;
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
                    ToastsMessage.showMessage("Đã xoá món: " + (new CuisineDAO()).getById(cuisineID).getName() + " ra khỏi danh sách", "success");
                }
                break;
            }
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

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

            if (existingCuisineID.equals(cuisineID)) {
                jsonObject.addProperty("Cuisine Note", newNote);
                ToastsMessage.showMessage("Đã cập nhật ghi chú cho món: " + (new CuisineDAO()).getById(cuisineID).getName() + " với nội dung là: " + newNote, "success");
                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.CUISINE_PATH);
    }


    @FXML
    void onDecreaseCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        decreaseQuantityInJSON(cuisineID);
        preOrderCuisineController.billGridPane.getChildren().clear();
        preOrderCuisineController.loadBill();
        //update lbl
        preOrderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onDeleteCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        removeFromJson(cuisineID);
        preOrderCuisineController.billGridPane.getChildren().clear();
        preOrderCuisineController.loadBill();
        //update lbl
        preOrderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onIncreaseCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        increaseQuantityInJSON(cuisineID);
        preOrderCuisineController.billGridPane.getChildren().clear();
        preOrderCuisineController.loadBill();
        //update lbl
        preOrderCuisineController.setCuisinesInfoFromJSON();
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
            preOrderCuisineController.billGridPane.getChildren().clear();
            try {
                preOrderCuisineController.loadBill();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
