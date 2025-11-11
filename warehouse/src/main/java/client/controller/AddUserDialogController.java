package client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.application.Platform;

import com.group9.warehouse.grpc.AddUserRequest;
import com.group9.warehouse.grpc.ServiceResponse;
import com.group9.warehouse.grpc.UserManagementServiceGrpc;

public class AddUserDialogController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label statusLabel;

    private Stage dialogStage;
    private UserManagementServiceGrpc.UserManagementServiceBlockingStub userManagementStub;
    private boolean saved = false;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Manager", "Staff"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUserManagementStub(UserManagementServiceGrpc.UserManagementServiceBlockingStub stub) {
        this.userManagementStub = stub;
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || role == null || fullName.isEmpty() || email.isEmpty()) {
            showStatus("Lỗi: Vui lòng nhập đầy đủ tất cả thông tin.", false);
            return;
        }

        try {
            AddUserRequest request = AddUserRequest.newBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .setRole(role)
                    .setFullName(fullName) 
                    .setEmail(email)       
                    .build();

            ServiceResponse response = userManagementStub.addUser(request);

            if (response.getSuccess()) {
                saved = true;
                dialogStage.close(); 
            } else {
                showStatus("Lỗi thêm user: " + response.getMessage(), false);
            }
        } catch (Exception e) {
            showStatus("Lỗi gRPC: " + e.getMessage(), false);
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private void showStatus(String message, boolean success) {
        Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setManaged(true);
            statusLabel.getStyleClass().removeAll("status-label-success", "status-label-error");
            if (success) {
                statusLabel.getStyleClass().add("status-label-success");
            } else {
                statusLabel.getStyleClass().add("status-label-error");
            }
        });
    }
}