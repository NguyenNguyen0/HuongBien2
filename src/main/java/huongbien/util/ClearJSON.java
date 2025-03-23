package huongbien.util;

import com.google.gson.JsonArray;
import huongbien.config.Constants;

public class ClearJSON {
    //NOTE: Don't delete this method, despite it's not used
    public static void clearAllJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.LOGIN_SESSION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.RESERVATION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
    }

    public static void clearAllJsonWithoutLoginSession() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
//      Utils.writeJsonToFile(new JsonArray(), Constants.LOGIN_SESSION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.RESERVATION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
    }

    public static void clearAllJsonWithoutLoginSession_PaymentQueue() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
//      Utils.writeJsonToFile(new JsonArray(), Constants.LOGIN_SESSION_PATH);
//      Utils.writeJsonToFile(new JsonArray(), Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.RESERVATION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
    }

    public static void clearCuisineJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
    }

    public static void clearCustomerJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
    }

    public static void clearLoginSessionJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.LOGIN_SESSION_PATH);
    }

    public static void clearPaymentQueueJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.PAYMENT_QUEUE_PATH);
    }

    public static void clearReservationJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.RESERVATION_PATH);
    }

    public static void clearTableJson() {
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
    }
}