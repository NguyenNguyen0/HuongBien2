package com.huongbien.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.huongbien.config.AppConfig;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.huongbien.entity.Customer;
import javafx.scene.control.Alert;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class QRCodeHandler {
    private final String username;
    private final String appPassword;

    public QRCodeHandler() {
        this.username = AppConfig.getEmailUsername();
        this.appPassword = AppConfig.getEmailPassword();
    }

    public void createQRCode(Customer selectedCustomer, String qrContent) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 400, 400, hints);

            String customerId = selectedCustomer.getCustomerId();
            String outputFile = "src/main/resources/com/huongbien/qrCode/QrCode_Ma" + customerId + ".png";
            Path path = Paths.get(outputFile);

            Files.createDirectories(path.getParent());

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "QR code được tạo thành công tại: " + outputFile);
            alert.showAndWait();
        } catch (WriterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi viết mã QR: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi ghi tệp: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi không xác định: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int channels = matrix.channels();
        byte[] data = new byte[cols * rows * channels];
        matrix.get(0, 0, data);
        BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    public String decodeQRCode(BufferedImage bufferedImage) {
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            QRCodeReader reader = new QRCodeReader();
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            return null;
        }
    }
}
