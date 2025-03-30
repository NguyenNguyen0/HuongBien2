package huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import huongbien.bus.CategoryBUS;
import huongbien.bus.CuisineBUS;
import huongbien.bus.EmployeeBUS;
import huongbien.bus.StatisticsBUS;
import huongbien.config.Constants;
import huongbien.dao.StatisticsDAO;
import huongbien.entity.Customer;
import huongbien.entity.Employee;
import huongbien.util.ToastsMessage;
import huongbien.util.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.LocalDateStringConverter;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class RestaurantStatisticsController {
    @FXML
    private BarChart<String, Number> revenueBarChart;
    @FXML
    private LineChart<String, Number> orderLineChart;
    @FXML
    private ComboBox<String> revenueStatisticalComboBox;
    @FXML
    private ComboBox<Integer> revenueYearComboBox;
    @FXML
    private ComboBox<String> cuisineStatisticalComboBox;
    @FXML
    private ComboBox<Integer> cuisineYearComboBox;
    @FXML
    private ComboBox<String> personalStatisticalComboBox;
    @FXML
    private ComboBox<Integer> personalYearComboBox;
    @FXML
    private Label totalRevenueLabel;
    @FXML
    private Label totalOrderLabel;
    @FXML
    private Label totalReservationLabel;
    @FXML
    private Label averageRevenueLabel;
    @FXML
    private Label totalCustomersField;
    @FXML
    private Label totalReservationField;
    @FXML
    private Label totalOrderField;
    @FXML
    private Label totalRevenuesField;
    @FXML
    private Label selectedYearLabel;
    @FXML
    private Tab customerStatisticTabPane;
    @FXML
    private Tab employeeStatisticTabPane;
    @FXML
    private Tab revenueStatisticTabPane;
    @FXML
    private Tab cuisineStatisticTabPane;
    @FXML
    private DatePicker dailyStatisticTimeDatePicker;
    @FXML
    private Label revenueYearLabel;
    @FXML
    private Label yearlyTotalOrderQuantityLabel;
    @FXML
    private Label yearlyGrowthRateLabel;
    @FXML
    private Label yearlyTotalRevenueLabel;
    @FXML
    private TableView<HashMap<String, Integer>> dailyRevenueTable;
    @FXML
    private TableColumn<HashMap<String, Integer>, Integer> dailyCustomerQuantityColumn;
    @FXML
    private TableColumn<HashMap<String, Integer>, Integer> dailyReservationQuantityColumn;
    @FXML
    private TableColumn<HashMap<String, Integer>, Integer> dailyOrderQuantityColumn;
    @FXML
    private TableColumn<HashMap<String, Integer>, String> dailyRevenueColumn;
    @FXML
    private Label topBestSellerCuisineLabel;
    @FXML
    private Label cuisineStatisticTitleLabel;
    @FXML
    private Label cuisineTotalRevenueLabel;
    @FXML
    private Label cuisineTotalSellLabel;
    @FXML
    private ComboBox<String> cuisineCategoryComboBox;
    @FXML
    private ComboBox<String> cuisineComboBox;
    @FXML
    private TableColumn<HashMap<String, String>, String> bestSellerCuisineCategoryColumn;
    @FXML
    private TableColumn<HashMap<String, String>, String> bestSellerCuisineIdColumn;
    @FXML
    private TableColumn<HashMap<String, String>, String> bestSellerCuisineNameColumn;
    @FXML
    private TableColumn<HashMap<String, String>, String> bestSellerCuisineRevenueColumn;
    @FXML
    private TableColumn<HashMap<String, String>, String> bestSellerCuisineSalePriceColumn;
    @FXML
    private TableView<HashMap<String, String>> bestSellerCuisineTable;
    @FXML
    private TableColumn<HashMap<String, String>, String> proportionCategoryNameColumn;
    @FXML
    private TableColumn<HashMap<String, String>, String> proportionCategoryRevenueColumn;
    @FXML
    private TableView<HashMap<String, String>> proportionRevenueTable;
    @FXML
    private PieChart revenueShareByCategoryPieChart;
    @FXML
    private LineChart<String, Number> cuisineRevenueLineChart;
    @FXML
    private ComboBox<Integer> customerYearComboBox;
    @FXML
    private ComboBox<String> customerStatisticalComboBox;
    @FXML
    private Label customerStatisticInfoTitleLabel;
    @FXML
    private Label customerTotalQuantityLabel;
    @FXML
    private Label newCustomerQuantityLabel;
    @FXML
    private Label newCustomerTableLabel;
    @FXML
    private Label topMembershipCustomerTableLabel;
    @FXML
    private TableView<Customer> newCustomerTable;
    @FXML
    private TableColumn<Customer, String> newCustomerIdColumn;
    @FXML
    private TableColumn<Customer, String> newCustomerNameColumn;
    @FXML
    private TableColumn<Customer, String> newCustomerPhoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> newCustomerAccumulatedPointColumn;
    @FXML
    private TableColumn<Customer, String> newCustomerMembershipLevelColumn;
    @FXML
    private TableView<Customer> topMembershipCustomerTable;
    @FXML
    private TableColumn<Customer, String> topCustomerIdColumn;
    @FXML
    private TableColumn<Customer, String> topCustomerNameColumn;
    @FXML
    private TableColumn<Customer, String> topCustomerPhoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> topCustomerAccumulatedPointColumn;
    @FXML
    private TableColumn<Customer, String> topCustomerMembershipLevelColumn;
    @FXML
    private LineChart<String, Number> newCustomerQuantityLineChart;
    @FXML
    private Label personalStatisticTitleLabel;
    @FXML
    private Label personalEmployeeNameLabel;
    @FXML
    private Label personalTotalRevenueLabel;
    @FXML
    private Label personalTotalOrderLabel;
    @FXML
    private Label personalTotalReservationLabel;
    @FXML
    private BarChart<String, Number> personalOrderAndReservationQuantityBarChart;
    @FXML
    private BarChart<String, Number> personalRevenueBarChart;

    @FXML
    public void initialize() {
//      Global statistical
        setBusinessSummary();
        setBusinessSituation();
        setStatisticalComboBoxes();
        setStatisticTabPaneChange();

//        Revenue statistical
        setDailyStatisticTimeDatePicker();
        setDailyRevenuesTableColumn();
        fillDataToDailyRevenueTable();
        setYearlyRevenueLabels(Year.now().getValue());
        updateRevenueBarChart("Tháng", Year.now().getValue());
        updateOrderLineChart("Tháng", Year.now().getValue());

//        Cuisine statistical
        setCuisineCategoryComboBox();
        setCuisineComboBox();
        setCuisineStatisticLabels();
        setBestSellerCuisineTableColumn();
        setProportionRevenueTableColumn();

//        Customer statistical
        setNewCustomerTableColumn();
        setTopMembershipCustomerTableColumn();

//        Employee statistical
        setPersonalRevenueAndOrderQuantityBarChart();
        setPersonalRevenueBarChart();
    }

    private void setBusinessSummary() {
        totalCustomersField.setText(String.valueOf(StatisticsDAO.getTotalCustomers()));
        totalReservationField.setText(String.valueOf(StatisticsDAO.getTotalReservations()));
        totalOrderField.setText(String.valueOf(StatisticsDAO.getTotalOrders()));
        totalRevenuesField.setText(Utils.formatMoney(StatisticsDAO.getTotalRevenue()));
    }

    private void setBusinessSituation() {
        totalRevenueLabel.setText(Utils.formatMoney(StatisticsDAO.getTotalRevenue()));
        totalReservationLabel.setText(StatisticsDAO.getTotalReservations() + " Đơn");
        totalOrderLabel.setText(StatisticsDAO.getTotalOrders() + " Đơn");
        averageRevenueLabel.setText(Utils.formatMoney(StatisticsDAO.getAverageRevenuePerOrder()));
    }

    public void setTablePlaceholder(TableView<?> tableView, String message) {
        String style = "-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: lightgray;";
        Label placeholder = new Label(message);
        placeholder.setStyle(style);
        tableView.setPlaceholder(placeholder);
    }

    private void setStatisticalComboBox(ComboBox<String> statisticalComboBox, ComboBox<Integer> yearComboBox) {
        statisticalComboBox.getItems().addAll("Tháng", "Quý", "Năm");
        statisticalComboBox.getSelectionModel().select("Tháng");
        int currentYear = Year.now().getValue();
        yearComboBox.getItems().addAll(IntStream.rangeClosed(StatisticsBUS.getFirstYear(), currentYear).boxed().toList());
        yearComboBox.getSelectionModel().selectLast();
    }

    private void setStatisticalComboBoxes() {
        setStatisticalComboBox(revenueStatisticalComboBox, revenueYearComboBox);

        setStatisticalComboBox(cuisineStatisticalComboBox, cuisineYearComboBox);

        setStatisticalComboBox(personalStatisticalComboBox, personalYearComboBox);

        setStatisticalComboBox(customerStatisticalComboBox, customerYearComboBox);
    }

    private void setStatisticTabPaneChange() {
        cuisineStatisticTabPane.setOnSelectionChanged(event -> {
            if (cuisineStatisticTabPane.isSelected()) {
                fillDataToBestSellerCuisineTable();
                fillDataToProportionRevenueTable();
                updateRevenueShareByCategoryPieChart();
                updateCuisineRevenueLineChart();
            }
        });

        customerStatisticTabPane.setOnSelectionChanged(event -> {
            if (customerStatisticTabPane.isSelected()) {
                setCustomerStatisticLabels();
                fillDataToNewCustomerTable();
                fillDataToTopMembershipCustomerTable();
                updateNewCustomerLineChart();
            }
        });

        employeeStatisticTabPane.setOnSelectionChanged(event -> {
            if (employeeStatisticTabPane.isSelected()) {
                setPersonalStatisticLabels();
                updatePersonalBarChart();
            }
        });
    }

    //    START: REVENUE STATISTICAL
    public void setYearlyRevenueLabels(int year) {
        HashMap<String, String> yearlyStat = StatisticsBUS.getYearStat(year);
        revenueYearLabel.setText("DOANH THU NĂM " + year);
        yearlyTotalOrderQuantityLabel.setText(yearlyStat.get("totalOrders") + " Đơn");
        yearlyTotalRevenueLabel.setText(yearlyStat.get("totalRevenue"));
        yearlyGrowthRateLabel.setText(yearlyStat.get("growthRate"));
    }

    public void setDailyStatisticTimeDatePicker() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dailyStatisticTimeDatePicker.setConverter(new LocalDateStringConverter(dateFormatter, null));
        dailyStatisticTimeDatePicker.setPromptText(dateFormatter.toString());
        dailyStatisticTimeDatePicker.setValue(LocalDate.now());
    }

    private void updateRevenueBarChart(String criteria, int year) {
        revenueBarChart.getData().clear();
        revenueBarChart.getYAxis().setLabel("Đồng");
        revenueBarChart.getXAxis().setLabel(criteria);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo " + criteria);

        if ("Tháng".equals(criteria)) {
            List<HashMap<String, Integer>> monthlyRevenues = StatisticsBUS.getMonthlyRevenues(year);
            for (HashMap<String, Integer> monthlyRevenue : monthlyRevenues) {
                series.getData().add(new XYChart.Data<>("Tháng " + monthlyRevenue.get("month"), monthlyRevenue.get("totalRevenue")));
            }
        } else if ("Quý".equals(criteria)) {
            List<HashMap<String, Integer>> quarterRevenues = StatisticsBUS.getQuarterRevenues(year);
            for (HashMap<String, Integer> quarterRevenue : quarterRevenues) {
                series.getData().add(new XYChart.Data<>("Quý " + quarterRevenue.get("quarter"), quarterRevenue.get("totalRevenue")));
            }
        } else if ("Năm".equals(criteria)) {
            List<HashMap<String, Integer>> yearlyRevenues = StatisticsBUS.getYearlyRevenues(StatisticsBUS.getFirstYear(), LocalDate.now().getYear()); // TODO: sửa lại năm bắt đầu
            for (HashMap<String, Integer> yearlyRevenue : yearlyRevenues) {
                series.getData().add(new XYChart.Data<>("Năm " + yearlyRevenue.get("year"), yearlyRevenue.get("totalRevenue")));
            }
        }
        revenueBarChart.getData().add(series);
    }

    private void updateOrderLineChart(String criteria, int year) {
        orderLineChart.getData().clear();
        orderLineChart.getXAxis().setLabel(criteria);
        orderLineChart.getYAxis().setLabel("Đồng");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng hóa đơn theo " + criteria);
        if ("Tháng".equals(criteria)) {
            List<HashMap<String, Integer>> monthlyOrderQuantities = StatisticsBUS.getMonthlyOrderQuantity(year);
            for (HashMap<String, Integer> monthlyOrderQuantity : monthlyOrderQuantities) {
                series.getData().add(new XYChart.Data<>("Tháng " + monthlyOrderQuantity.get("month"), monthlyOrderQuantity.get("orderQuantity")));
            }
        } else if ("Quý".equals(criteria)) {
            List<HashMap<String, Integer>> quarterOrderQuantities = StatisticsBUS.getQuarterOrderQuantity(year);
            for (HashMap<String, Integer> quarterOrderQuantity : quarterOrderQuantities) {
                series.getData().add(new XYChart.Data<>("Quý " + quarterOrderQuantity.get("quarter"), quarterOrderQuantity.get("orderQuantity")));
            }
        } else if ("Năm".equals(criteria)) {
            List<HashMap<String, Integer>> yearlyOrderQuantities = StatisticsBUS.getYearlyOrderQuantity(StatisticsBUS.getFirstYear(), LocalDate.now().getYear());
            for (HashMap<String, Integer> yearlyOrderQuantity : yearlyOrderQuantities) {
                series.getData().add(new XYChart.Data<>("Năm " + yearlyOrderQuantity.get("year"), yearlyOrderQuantity.get("orderQuantity")));
            }
        }
        orderLineChart.getData().add(series);
    }

    public void setDailyRevenuesTableColumn() {
        setTablePlaceholder(dailyRevenueTable, "Không có dữ liệu nào để hiển thị");
        dailyRevenueColumn.setCellValueFactory(cellData -> {
            HashMap<String, Integer> data = cellData.getValue();
            return new SimpleObjectProperty<>(Utils.formatMoney(data.get("totalRevenue") != null ? data.get("totalRevenue") : 0));
        });
        dailyReservationQuantityColumn.setCellValueFactory(cellData -> {
            HashMap<String, Integer> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("totalReservations"));
        });
        dailyOrderQuantityColumn.setCellValueFactory(cellData -> {
            HashMap<String, Integer> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("totalOrders"));
        });
        dailyCustomerQuantityColumn.setCellValueFactory(cellData -> {
            HashMap<String, Integer> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("newCustomers"));
        });
    }

    public void fillDataToDailyRevenueTable() {
        dailyRevenueTable.getItems().clear();
        LocalDate date = dailyStatisticTimeDatePicker.getValue();
        HashMap<String, Integer> dailyRevenue = StatisticsBUS.getDailyRevenue(date);
        if (dailyRevenue.get("totalRevenue") == null) {
            ToastsMessage.showMessage("Không có dữ liệu ngày " + date, "warning");
            return;
        }
        dailyRevenueTable.getItems().add(dailyRevenue);
    }

    @FXML
    public void onRevenueYearComboBoxClicked(ActionEvent actionEvent) {
        String criteria = revenueStatisticalComboBox.getValue();
        int year = revenueYearComboBox.getValue();
        setYearlyRevenueLabels(year);
        updateRevenueBarChart(criteria, year);
        updateOrderLineChart(criteria, year);
    }

    @FXML
    public void onRevenueStatisticalComboBoxClicked(ActionEvent actionEvent) {
        String criteria = revenueStatisticalComboBox.getValue();
        int year = revenueYearComboBox.getValue();
        updateRevenueBarChart(criteria, year);
        updateOrderLineChart(criteria, year);
    }

    @FXML
    public void onPrevDayButtonClicked(MouseEvent mouseEvent) {
        dailyStatisticTimeDatePicker.setValue(dailyStatisticTimeDatePicker.getValue().minusDays(1));
    }

    @FXML
    public void onDailyStatisticTimeDatePickerSelected(ActionEvent actionEvent) {
        if (dailyStatisticTimeDatePicker.getValue().isAfter(LocalDate.now())) {
            dailyRevenueTable.getItems().clear();
            ToastsMessage.showMessage("Không thể xem dữ liệu trong tương lai", "error");
        } else {
            fillDataToDailyRevenueTable();
        }
    }

    @FXML
    public void onNextDayButtonClicked(MouseEvent mouseEvent) {
        dailyStatisticTimeDatePicker.setValue(dailyStatisticTimeDatePicker.getValue().plusDays(1));
    }

//    END: REVENUE STATISTICAL

    //    START: CUISINE STATISTICAL
    public void setBestSellerCuisineTableColumn() {
        setTablePlaceholder(bestSellerCuisineTable, "Không có dữ liệu nào để hiển thị");
        bestSellerCuisineIdColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("cuisineId"));
        });
        bestSellerCuisineNameColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("cuisineName"));
        });
        bestSellerCuisineCategoryColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("categoryName"));
        });
        bestSellerCuisineSalePriceColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("salePrice"));
        });
        bestSellerCuisineRevenueColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("totalRevenue"));
        });
    }

    public void fillDataToBestSellerCuisineTable() {
        bestSellerCuisineTable.getItems().clear();
        int limitTop = 10;
        int year = cuisineYearComboBox.getSelectionModel().getSelectedItem();
        topBestSellerCuisineLabel.setText("TOP " + limitTop + " MÓN ĂN BÁN CHẠY NHẤT NĂM " + year);
        List<HashMap<String, String>> bestSellerCuisines = StatisticsBUS.getBestSellerCuisines(year, limitTop);
        if (bestSellerCuisines.isEmpty()) {
            ToastsMessage.showMessage("Không có dữ liệu", "warning");
            return;
        }
        bestSellerCuisineTable.getItems().addAll(bestSellerCuisines);
    }

    public void setProportionRevenueTableColumn() {
        setTablePlaceholder(proportionRevenueTable, "Không có dữ liệu nào để hiển thị");
        proportionCategoryNameColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("categoryName"));
        });
        proportionCategoryRevenueColumn.setCellValueFactory(cellData -> {
            HashMap<String, String> data = cellData.getValue();
            return new SimpleObjectProperty<>(data.get("categoryRevenue"));
        });
    }

    public void setCuisineCategoryComboBox() {
        CategoryBUS categoryBUS = new CategoryBUS();
        cuisineCategoryComboBox.getItems().addAll(categoryBUS.getAllCategoryNames());
        cuisineCategoryComboBox.getSelectionModel().selectFirst();
    }

    public void setCuisineComboBox() {
        CuisineBUS cuisineBUS = new CuisineBUS();
        String categoryName = cuisineCategoryComboBox.getSelectionModel().getSelectedItem();
        cuisineComboBox.getItems().clear();
        cuisineComboBox.getItems().add("Tất cả");
        cuisineComboBox.getItems().addAll(cuisineBUS.getCuisineNamesByCategory(categoryName));
        cuisineComboBox.setValue("Tất cả");
    }

    public void setCuisineStatisticLabels() {
        String cuisineName = cuisineComboBox.getSelectionModel().getSelectedItem();
        String criteria = cuisineStatisticalComboBox.getSelectionModel().getSelectedItem();
        String categoryName = cuisineCategoryComboBox.getSelectionModel().getSelectedItem();
        int year = cuisineYearComboBox.getSelectionModel().getSelectedItem();

        cuisineName = cuisineName == null ? "Tất cả" : cuisineName;
        if ("Tất cả".equals(cuisineName)) {
            cuisineStatisticTitleLabel.setText("Thống kê doanh thu của tất cả món ăn");
        } else {
            cuisineStatisticTitleLabel.setText("Thống kê doanh thu của món " + cuisineName);
        }
        cuisineTotalRevenueLabel.setText(Utils.formatMoney(StatisticsBUS.getCuisineRevenue(cuisineName, categoryName, criteria, year)));
        cuisineTotalSellLabel.setText(StatisticsBUS.getCuisineTotalSell(cuisineName, categoryName, criteria, year) + " Món");
    }

    public void fillDataToProportionRevenueTable() {
        proportionRevenueTable.getItems().clear();
        int year = cuisineYearComboBox.getSelectionModel().getSelectedItem();
        List<HashMap<String, String>> revenueProportionByCategory = StatisticsBUS.getRevenueProportionByCategory(year);
        if (revenueProportionByCategory.isEmpty()) {
            ToastsMessage.showMessage("Không có dữ liệu", "warning");
            return;
        }
        proportionRevenueTable.getItems().addAll(revenueProportionByCategory);
    }

    public void updateRevenueShareByCategoryPieChart() {
        int year = cuisineYearComboBox.getSelectionModel().getSelectedItem();
        List<HashMap<String, String>> revenueProportionByCategory = StatisticsBUS.getRevenueProportionByCategory(year);
        revenueShareByCategoryPieChart.getData().clear();

        List<PieChart.Data> dataList = new ArrayList<>();
        for (HashMap<String, String> category : revenueProportionByCategory) {
            PieChart.Data data = new PieChart.Data(category.get("categoryName"), Double.parseDouble(category.get("revenuePercentage")));
            dataList.add(data);
        }

        revenueShareByCategoryPieChart.setData(FXCollections.observableList(dataList));
    }

    public void updateCuisineRevenueLineChart() {
        String categoryName = cuisineCategoryComboBox.getSelectionModel().getSelectedItem();
        String cuisineName = cuisineComboBox.getSelectionModel().getSelectedItem();
        String criteria = cuisineStatisticalComboBox.getSelectionModel().getSelectedItem();
        int year = cuisineYearComboBox.getSelectionModel().getSelectedItem();

        if (categoryName == null || cuisineName == null || criteria == null) {
            return;
        }

        cuisineRevenueLineChart.getData().clear();
        cuisineRevenueLineChart.getXAxis().setLabel(criteria);
        cuisineRevenueLineChart.getYAxis().setLabel("Đồng");
        switch (criteria) {
            case "Quý" -> {
                if (cuisineName.equals("Tất cả")) {
                    CuisineBUS cuisineBUS = new CuisineBUS();
                    List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                    for (String name : cuisineNames) {
                        List<HashMap<String, Integer>> cuisineRevenues = StatisticsBUS.getQuarterCuisineRevenues(name, year);
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        series.setName(name);
                        for (HashMap<String, Integer> cuisineRevenue : cuisineRevenues) {
                            series.getData().add(new XYChart.Data<>("Quý " + cuisineRevenue.get("quarter"), cuisineRevenue.get("revenue")));
                        }
                        cuisineRevenueLineChart.getData().add(series);
                    }
                } else {
                    List<HashMap<String, Integer>> cuisineRevenues = StatisticsBUS.getQuarterCuisineRevenues(cuisineName, year);
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(cuisineName);
                    for (HashMap<String, Integer> cuisineRevenue : cuisineRevenues) {
                        series.getData().add(new XYChart.Data<>("Quý " + cuisineRevenue.get("quarter"), cuisineRevenue.get("revenue")));
                    }
                    cuisineRevenueLineChart.getData().add(series);
                }
            }
            case "Tháng" -> {
                if (cuisineName.equals("Tất cả")) {
                    CuisineBUS cuisineBUS = new CuisineBUS();
                    List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                    for (String name : cuisineNames) {
                        List<HashMap<String, Integer>> cuisineRevenues = StatisticsBUS.getMonthlyCuisineRevenues(name, year);
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        series.setName(name);
                        for (HashMap<String, Integer> cuisineRevenue : cuisineRevenues) {
                            series.getData().add(new XYChart.Data<>("Tháng " + cuisineRevenue.get("month"), cuisineRevenue.get("revenue")));
                        }
                        cuisineRevenueLineChart.getData().add(series);
                    }
                } else {
                    List<HashMap<String, Integer>> cuisineRevenues = StatisticsBUS.getMonthlyCuisineRevenues(cuisineName, year);
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(cuisineName);
                    for (HashMap<String, Integer> cuisineRevenue : cuisineRevenues) {
                        series.getData().add(new XYChart.Data<>("Tháng " + cuisineRevenue.get("month"), cuisineRevenue.get("revenue")));
                    }
                    cuisineRevenueLineChart.getData().add(series);
                }
            }
            case "Năm" -> {
                if (cuisineName.equals("Tất cả")) {
                    CuisineBUS cuisineBUS = new CuisineBUS();
                    List<String> cuisineNames = cuisineBUS.getCuisineNamesByCategory(categoryName);
                    for (String name : cuisineNames) {
                        List<HashMap<String, Integer>> cuisineRevenues = StatisticsBUS.getYearlyCuisineRevenues(name, StatisticsBUS.getFirstYear(), LocalDate.now().getYear());
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        series.setName(name);
                        for (HashMap<String, Integer> cuisineRevenue : cuisineRevenues) {
                            series.getData().add(new XYChart.Data<>("Năm " + cuisineRevenue.get("year"), cuisineRevenue.get("revenue")));
                        }
                        cuisineRevenueLineChart.getData().add(series);
                    }
                } else {
                    List<HashMap<String, Integer>> cuisineRevenues = StatisticsBUS.getYearlyCuisineRevenues(cuisineName, StatisticsBUS.getFirstYear(), LocalDate.now().getYear());
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(cuisineName);
                    for (HashMap<String, Integer> cuisineRevenue : cuisineRevenues) {
                        series.getData().add(new XYChart.Data<>("Năm " + cuisineRevenue.get("year"), cuisineRevenue.get("revenue")));
                    }
                    cuisineRevenueLineChart.getData().add(series);
                }
            }
        }
    }

    @FXML
    public void onCuisineCategoryComboBoxSelected(ActionEvent event) {
        setCuisineComboBox();
    }

    @FXML
    public void onCuisineComboBoxSelected(ActionEvent event) {
        updateCuisineRevenueLineChart();
        setCuisineStatisticLabels();
    }

    @FXML
    public void onCuisineStatisticalComboBoxSelected(ActionEvent event) {
        updateCuisineRevenueLineChart();
        setCuisineStatisticLabels();
    }

    @FXML
    public void onCuisineYearComboBoxSelected(ActionEvent event) {
        fillDataToBestSellerCuisineTable();
        fillDataToProportionRevenueTable();
        updateRevenueShareByCategoryPieChart();
        updateCuisineRevenueLineChart();
        setCuisineStatisticLabels();
    }

//    END: CUISINE STATISTICAL

    //    START: CUSTOMER STATISTICAL
    public void setNewCustomerTableColumn() {
        setTablePlaceholder(newCustomerTable, "Không có dữ liệu nào để hiển thị");
        newCustomerIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        newCustomerNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        newCustomerPhoneNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
        newCustomerAccumulatedPointColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAccumulatedPoints()).asString());
        newCustomerMembershipLevelColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMembershipLevel()).asString());
    }

    public void fillDataToNewCustomerTable() {
        newCustomerTable.getItems().clear();
        int year = customerYearComboBox.getSelectionModel().getSelectedItem();
        newCustomerTableLabel.setText("KHÁCH HÀNG MỚI NĂM " + year);

        List<Customer> newCustomers = StatisticsBUS.getNewCustomers(year);
        if (newCustomers.isEmpty()) {
            ToastsMessage.showMessage("Không có dữ liệu", "warning");
            return;
        }
        newCustomerTable.getItems().addAll(newCustomers);
    }

    public void setTopMembershipCustomerTableColumn() {
        setTablePlaceholder(topMembershipCustomerTable, "Không có dữ liệu nào để hiển thị");
        topCustomerIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        topCustomerNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        topCustomerPhoneNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
        topCustomerAccumulatedPointColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAccumulatedPoints()).asString());
        topCustomerMembershipLevelColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMembershipLevel()).asString());
    }

    public void fillDataToTopMembershipCustomerTable() {
        topMembershipCustomerTable.getItems().clear();
        int year = customerYearComboBox.getSelectionModel().getSelectedItem();
        int limit = 10;
        topMembershipCustomerTableLabel.setText(String.format("TOP %d KHÁCH HÀNG THÀNH VIÊN NĂM %d", limit, year));
        List<Customer> topMembershipCustomers = StatisticsBUS.getTopMembershipCustomers(year, limit);
        if (topMembershipCustomers.isEmpty()) {
            ToastsMessage.showMessage("Không có dữ liệu", "warning");
            return;
        }
        topMembershipCustomerTable.getItems().addAll(topMembershipCustomers);
    }

    public void setCustomerStatisticLabels() {
        int year = customerYearComboBox.getSelectionModel().getSelectedItem();
        int totalCustomers = StatisticsBUS.getTotalCustomersQuantityByYear(year);
        int newCustomers = StatisticsBUS.getNewCustomers(year).size();
        customerStatisticInfoTitleLabel.setText("THÔNG TIN SỐ LƯỢNG KHÁCH HÀNG NĂM " + year);
        customerTotalQuantityLabel.setText(totalCustomers + " KH");
        newCustomerQuantityLabel.setText(newCustomers + " KH");
    }

    public void updateNewCustomerLineChart() {
        String criteria = customerStatisticalComboBox.getSelectionModel().getSelectedItem();

        newCustomerQuantityLineChart.getData().clear();
        newCustomerQuantityLineChart.getXAxis().setLabel(criteria);
        newCustomerQuantityLineChart.getYAxis().setLabel("Khách hàng");

        switch (criteria) {
            case "Tháng" -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Khách hàng mới theo tháng");
                int year = customerYearComboBox.getSelectionModel().getSelectedItem();
                List<HashMap<String, Integer>> monthlyNewCustomers = StatisticsBUS.getMonthlyNewCustomers(year);
                for (HashMap<String, Integer> monthlyNewCustomer : monthlyNewCustomers) {
                    series.getData().add(new XYChart.Data<>("Tháng " + monthlyNewCustomer.get("month"), monthlyNewCustomer.get("newCustomerQuantity")));
                }
                newCustomerQuantityLineChart.getData().add(series);
            }
            case "Quý" -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Khách hàng mới theo quý");
                int year = customerYearComboBox.getSelectionModel().getSelectedItem();
                List<HashMap<String, Integer>> quarterNewCustomers = StatisticsBUS.getQuarterNewCustomer(year);
                for (HashMap<String, Integer> quarterNewCustomer : quarterNewCustomers) {
                    series.getData().add(new XYChart.Data<>("Quý " + quarterNewCustomer.get("quarter"), quarterNewCustomer.get("newCustomerQuantity")));
                }
                newCustomerQuantityLineChart.getData().add(series);
            }
            case "Năm" -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Khách hàng mới theo năm");
                List<HashMap<String, Integer>> yearlyNewCustomers = StatisticsBUS.getYearlyNewCustomer(StatisticsBUS.getFirstYear(), LocalDate.now().getYear());
                for (HashMap<String, Integer> yearlyNewCustomer : yearlyNewCustomers) {
                    series.getData().add(new XYChart.Data<>("Năm " + yearlyNewCustomer.get("year"), yearlyNewCustomer.get("newCustomerQuantity")));
                }
                newCustomerQuantityLineChart.getData().add(series);
            }
        }
    }

    @FXML
    public void onCustomerYearComboBoxSelected(ActionEvent actionEvent) {
        fillDataToNewCustomerTable();
        fillDataToTopMembershipCustomerTable();
        setCustomerStatisticLabels();
        updateNewCustomerLineChart();
    }

    @FXML
    public void onCustomerStatisticalComboBoxSelected(ActionEvent actionEvent) {
        updateNewCustomerLineChart();
    }

//    END: CUSTOMER STATISTICAL

    //    START: EMPLOYEE STATISTICAL
    public void setPersonalStatisticLabels() {
        String currentLoginSessionEmployee = null;
        String employeeId = null;
        try {
            JsonArray jsonArraySession = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
            for (JsonElement element : jsonArraySession) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("Employee ID").getAsString();
                EmployeeBUS employeeBUS = new EmployeeBUS();
                Employee employee = employeeBUS.getEmployeeById(id).get(0);
                if (employee != null) {
                    currentLoginSessionEmployee = employee.getName();
                    employeeId = id;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (currentLoginSessionEmployee == null) {
            ToastsMessage.showMessage("Yêu cầu đăng nhập", "error");
            return;
        }

        int year = personalYearComboBox.getSelectionModel().getSelectedItem();

        double personalRevenue = StatisticsBUS.getPersonalRevenue(employeeId, year);
        int personalTotalOrder = StatisticsBUS.getPersonalTotalOrder(employeeId, year);
        int personalTotalReservation = StatisticsBUS.getPersonalTotalReservation(employeeId, year);
        personalStatisticTitleLabel.setText("TỔNG DOANH SỐ CÁ NHÂN NĂM " + year);
        personalEmployeeNameLabel.setText(currentLoginSessionEmployee);
        personalTotalRevenueLabel.setText(Utils.formatMoney(personalRevenue));
        personalTotalOrderLabel.setText(personalTotalOrder + " Đơn");
        personalTotalReservationLabel.setText(personalTotalReservation + " Đơn");
    }

    public void setPersonalRevenueAndOrderQuantityBarChart() {
        personalOrderAndReservationQuantityBarChart.setTitle("SỐ LƯỢNG HÓA ĐƠN VÀ ĐƠN ĐẶT BÀN THEO NHÂN VIÊN");
        personalOrderAndReservationQuantityBarChart.getYAxis().setLabel("Đơn");
    }

    public void setPersonalRevenueBarChart() {
        personalRevenueBarChart.setTitle("DOANH THU THEO NHÂN VIÊN");
        personalRevenueBarChart.getYAxis().setLabel("Triệu Đồng");
    }

    public void updatePersonalBarChart() {
        String currentLoginSessionEmployee = null;
        String employeeId = null;
        try {
            JsonArray jsonArraySession = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
            for (JsonElement element : jsonArraySession) {
                JsonObject jsonObject = element.getAsJsonObject();
                String id = jsonObject.get("Employee ID").getAsString();
                EmployeeBUS employeeBUS = new EmployeeBUS();
                Employee employee = employeeBUS.getEmployeeById(id).get(0);
                if (employee != null) {
                    currentLoginSessionEmployee = employee.getName();
                    employeeId = id;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (currentLoginSessionEmployee == null) {
            ToastsMessage.showMessage("Yêu cầu đăng nhập", "error");
            return;
        }

        String criteria = personalStatisticalComboBox.getSelectionModel().getSelectedItem();
        int year = personalYearComboBox.getSelectionModel().getSelectedItem();

        Function<String, String> converter;
        String timeKey;
        switch (criteria) {
            case "Tháng" -> {
                converter = x -> "Tháng " + x;
                timeKey = "month";
            }
            case "Quý" -> {
                converter = x -> "Quý " + x;
                timeKey = "quarter";
            }
            case "Năm" -> {
                converter = x -> "Năm " + x;
                timeKey = "year";
            }
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
        }

        List<HashMap<String, String>> personalRevenueAndOrder = StatisticsBUS.getPersonalRevenueAndOrder(employeeId, criteria, year);

        XYChart.Series<String, Number> personalRevenue = new XYChart.Series<>();
        XYChart.Series<String, Number> personalTotalOrder = new XYChart.Series<>();
        XYChart.Series<String, Number> personalTotalReservation = new XYChart.Series<>();
        personalRevenue.setName("Doanh thu");
        personalTotalOrder.setName("Hóa đơn");
        personalTotalReservation.setName("Đơn bàn");
        for (HashMap<String, String> stat : personalRevenueAndOrder) {
            personalRevenue.getData().add(new XYChart.Data<>(converter.apply(stat.get(timeKey)), Double.parseDouble(stat.get("revenue")) / 1_000_000));
            personalTotalOrder.getData().add(new XYChart.Data<>(converter.apply(stat.get(timeKey)), Integer.valueOf(stat.get("totalOrder"))));
            personalTotalReservation.getData().add(new XYChart.Data<>(converter.apply(stat.get(timeKey)), Integer.valueOf(stat.get("totalReservation"))));
        }

        personalOrderAndReservationQuantityBarChart.getData().clear();
        personalOrderAndReservationQuantityBarChart.getData().add(personalTotalOrder);
        personalOrderAndReservationQuantityBarChart.getData().add(personalTotalReservation);

        personalRevenueBarChart.getData().clear();
        personalRevenueBarChart.getData().add(personalRevenue);
    }

    @FXML
    public void onPersonalYearComboBoxSelected(ActionEvent actionEvent) {
        setPersonalStatisticLabels();
        updatePersonalBarChart();
    }

    @FXML
    public void onPersonalStatisticalComboBoxSelected(ActionEvent actionEvent) {
        updatePersonalBarChart();
    }

//    END: EMPLOYEE STATISTICAL
}
