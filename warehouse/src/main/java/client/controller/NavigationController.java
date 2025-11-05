package client.controller;

import client.service.SessionManager;
import client.service.SocketService;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class NavigationController {
    @FXML
    private Button reportsButton;
    @FXML
    private Button productMgmtButton;
    @FXML
    private Button logoutButton;

    private static MainAppWindowController mainController;

    public static void setMainController(MainAppWindowController controller) {
        mainController = controller;
    }

    @FXML
    public void initialize() {
        boolean isManager = SessionManager.isManager();
        reportsButton.setVisible(isManager);
        reportsButton.setManaged(isManager);
        productMgmtButton.setVisible(isManager);
        productMgmtButton.setManaged(isManager);
    }

    @FXML
    private void loadWarehouseView() {
        mainController.loadView("/client/view/WarehouseView.fxml");
    }

    @FXML
    private void loadHistoryView() {
        mainController.loadView("/client/view/HistoryView.fxml");
    }

    @FXML
    private void loadReportsView() {
        mainController.loadView("/client/view/ReportsView.fxml");
    }

    @FXML
    private void loadProductMgmtView() {
        // mainController.loadView("/client/view/ProductManagerClientView.fxml");
        System.out.println("Tải trang Quản lý Sản phẩm...");
    }
    
    @FXML
    private void handleLogout() {
        try{
            SessionManager.clearSession();
            SocketService.getInstance().close();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/Login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Hệ thống Quản lý Kho - Đăng nhập");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
