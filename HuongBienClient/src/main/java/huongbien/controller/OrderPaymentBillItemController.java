package huongbien.controller;

import huongbien.entity.OrderDetail;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderPaymentBillItemController {
    //Controller area
    public OrderPaymentController orderPaymentController;
    public OrderPaymentFinalController orderPaymentFinalController;
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

    public void setOrderPaymentController(OrderPaymentController orderPaymentController) {
        this.orderPaymentController = orderPaymentController;
    }

    public void setOrderPaymentFinalController(OrderPaymentFinalController orderPaymentFinalController) {
        this.orderPaymentFinalController = orderPaymentFinalController;
    }

    public void setDataBill(OrderDetail orderDetail) {
        cuisineIdLabel.setText(orderDetail.getCuisine().getId());
        cuisineNameLabel.setText(orderDetail.getCuisine().getName());
        cuisineSalePriceLabel.setText(String.format("%,.0f VNĐ", orderDetail.getCuisine().getPrice()));
        cuisineNoteLabel.setText(orderDetail.getNote());
        cuisineQuantityLabel.setText(orderDetail.getQuantity() + "");
        cuisineTotalPriceLabel.setText(String.format("%,.0f VNĐ", orderDetail.getSalePrice()));
    }
}
