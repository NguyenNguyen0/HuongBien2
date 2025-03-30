package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.config.Constants;
import huongbien.config.Variable;
import huongbien.dao.TableDAO;
import huongbien.entity.Table;
import huongbien.entity.TableStatus;
import huongbien.jpa.PersistenceUnit;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class OrderTableItemController {
    @FXML private ImageView tableImageView;
    @FXML private ImageView checkedImageView;
    @FXML private ImageView tableTypeImageView;
    @FXML private Label tableIdLabel; // Table ID displayed in the table item, get from the database !important
    @FXML private Label tableNameLabel;
    @FXML private Label tableSeatLabel;

    private boolean isCheck = false;

    //Controller area
    public OrderTableController orderTableController;
    public void setOrderTableController(OrderTableController orderTableController) {
        this.orderTableController = orderTableController;
    }

    public void setTableItemData(Table table) {
        tableIdLabel.setText(table.getId());
        tableNameLabel.setText(table.getName());
        tableSeatLabel.setText("Số chỗ: " + table.getSeats());
        setTableImage(table.getStatus(), table.getTableType().getId());
        setCheckedTableFromJSON();
    }

    private void setTableImage(TableStatus tableStatus, String tableType) {
//        String imagePath = switch (tableStatus) {
//            case Constants.tableReserved -> "/huongbien/icon/order/tableReserved-512px.png";
//            case Constants.tableOpen -> "/huongbien/icon/order/tableOpen-512px.png";
//            case Constants.tableClosed -> "/huongbien/icon/order/tableClosed-512px.png";
//            case Constants.tableEmpty -> "/huongbien/icon/order/tableEmpty-512px.png";
//            default -> "/huongbien/icon/order/tableEmpty-512px.png";
//        };
        String imagePath = switch (tableStatus) {
            case TableStatus.OCCUPIED -> "/huongbien/icon/order/tableReserved-512px.png";
            case TableStatus.RESERVED -> "/huongbien/icon/order/tableOpen-512px.png";
            case TableStatus.UNAVAILABLE -> "/huongbien/icon/order/tableClosed-512px.png";
            case TableStatus.AVAILABLE -> "/huongbien/icon/order/tableEmpty-512px.png";
        };
        assert imagePath != null;
        tableImageView.setImage(new Image(imagePath));
        if (tableType != null && tableType.equals(Variable.tableVipID)) {
            tableTypeImageView.setImage(new Image("/huongbien/icon/order/vip-128px.png"));
        } else {
            tableTypeImageView.setImage(null);
        }
    }

    private void setCheckedTableFromJSON() {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableIdLabel.getText())) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            checkedImageView.setImage(new Image("/huongbien/icon/order/check-128px.png"));
            isCheck = true;
        } else {
            checkedImageView.setImage(null);
            isCheck = false;
        }
    }

    public void updateCheckedIcon(String tableId) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableId)) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            checkedImageView.setImage(new Image("/huongbien/icon/order/check-128px.png"));
            isCheck = true;
        } else {
            checkedImageView.setImage(null);
            isCheck = false;
        }
    }

    private void writeDataToJSONFile(String tableId) {
        Table table = new TableDAO(PersistenceUnit.MARIADB_JPA).getById(tableId);
        if (table != null) {
            JsonArray jsonArray;
            try {
                jsonArray = Utils.readJsonFromFile(Constants.TABLE_PATH);
            } catch (FileNotFoundException e) {
                jsonArray = new JsonArray();
            }
            boolean tableExists = false;
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject existingTable = jsonArray.get(i).getAsJsonObject();
                if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableId)) {
                    jsonArray.remove(i);
                    tableExists = true;
                    break;
                }
            }
            if (!tableExists) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Table ID", table.getId());
                jsonArray.add(jsonObject);
            }
            Utils.writeJsonToFile(jsonArray, Constants.TABLE_PATH);
            updateCheckedIcon(tableId);
        } else {
            System.out.println("Table with ID " + tableId + " not found in the database.");
        }
    }

    @FXML
    void onOrderTableItemVBoxClicked(MouseEvent event) throws FileNotFoundException, SQLException {
        //check status of table
        String tableId = tableIdLabel.getText();
        Table table = new TableDAO(PersistenceUnit.MARIADB_JPA).getById(tableId);
        switch (table.getStatus()) {
//            case Constants.tableReserved -> ToastsMessage.showMessage("Bàn đang được đặt trước, không thể chọn bàn này.", "warning");
//            case Constants.tableOpen -> ToastsMessage.showMessage("Bàn đang được phục vụ, không thể chọn bàn này.", "warning");
//            case Constants.tableClosed -> ToastsMessage.showMessage("Bàn đã đóng, không thể chọn bàn này.", "warning");
//            case Constants.tableEmpty -> {
//                writeDataToJSONFile(tableId);
//                orderTableController.tableInfoTabPane.getTabs().clear();
//                orderTableController.readTableDataFromJSON();
//            }
            case TableStatus.OCCUPIED -> ToastsMessage.showMessage("Bàn đang được đặt trước, không thể chọn bàn này.", "warning");
            case TableStatus.RESERVED -> ToastsMessage.showMessage("Bàn đang được phục vụ, không thể chọn bàn này.", "warning");
            case TableStatus.UNAVAILABLE -> ToastsMessage.showMessage("Bàn đã đóng, không thể chọn bàn này.", "warning");
            case TableStatus.AVAILABLE -> {
                writeDataToJSONFile(tableId);
                orderTableController.tableInfoTabPane.getTabs().clear();
                orderTableController.readTableDataFromJSON();
            }
            default -> System.out.println("Unknown table status: " + table.getStatus().getValue());
        }
    }
}
