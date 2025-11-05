package client.controller;

import client.service.SocketService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WarehouseController {

    @FXML private ComboBox<String> productComboBox;
    @FXML private TextField quantityField;
    @FXML private Button refreshButton;
    @FXML private TableView<?> inventoryTable; // TODO: Thay bằng TableView<InventoryItem>
    @FXML private TableColumn<?, ?> productNameCol; // TODO: Cấu hình
    @FXML private TableColumn<?, ?> quantityCol; // TODO: Cấu hình
    @FXML private TextArea logTextArea;

    private SocketService socketService;

    @FXML
    public void initialize() {
        socketService = SocketService.getInstance();
        loadProducts();
        loadInventory();
    }

    private void loadProducts() {
        // TODO: Gửi lệnh "GET_PRODUCTS"
        // String response = socketService.sendRequest("GET_PRODUCTS");
        // Phân tích response và thêm vào productComboBox
        // Ví dụ: productComboBox.getItems().addAll("Laptop Dell", "Chuột Logitech");
        logTextArea.appendText("Đã tải danh sách sản phẩm...\n");
    }

    @FXML
    private void handleRefresh() {
        loadInventory();
    }
    
    private void loadInventory() {
        // TODO: Gửi lệnh "GET_INVENTORY"
        // String response = socketService.sendRequest("GET_INVENTORY");
        // Phân tích response và cập nhật inventoryTable
        logTextArea.appendText("Đã cập nhật bảng tồn kho...\n");
    }

    @FXML
    private void handleImportButton() {
        // TODO: Gửi lệnh "NHAP;...
        logTextArea.appendText("Đã gửi yêu cầu NHẬP HÀNG...\n");
        // Sau khi thành công, gọi loadInventory()
    }

    @FXML
    private void handleExportButton() {
        // TODO: Gửi lệnh "XUAT;...
        logTextArea.appendText("Đã gửi yêu cầu XUẤT HÀNG...\n");
        // Sau khi thành công, gọi loadInventory()
    }
}