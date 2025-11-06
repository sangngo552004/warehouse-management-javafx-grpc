package client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class ReportsController {

    @FXML private PieChart inventoryPieChart;
    @FXML private BarChart<?, ?> activityBarChart;

    @FXML
    public void initialize() {
        loadPieChartData();
        loadBarChartData();
    }

    private void loadPieChartData() {
        // TODO: Lấy dữ liệu từ "GET_INVENTORY"
        // Đây là data mẫu
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Laptop Dell", 90),
                        new PieChart.Data("Chuột Logitech", 490),
                        new PieChart.Data("Bàn phím cơ", 150));
        
        inventoryPieChart.setData(pieChartData);
    }
    
    private void loadBarChartData() {
        // TODO: Lấy dữ liệu từ "GET_HISTORY"
        // Đây là data mẫu
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Nhập");
        series1.getData().add(new XYChart.Data("Laptop", 50));
        series1.getData().add(new XYChart.Data("Chuột", 200));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Xuất");
        series2.getData().add(new XYChart.Data("Laptop", 20));
        series2.getData().add(new XYChart.Data("Chuột", 80));

        activityBarChart.getData().addAll(series1, series2);
    }
}