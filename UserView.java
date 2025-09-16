package view;

import controller.LoginController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.util.ArrayList;

public class UserView extends Application {

    private LoginController loginController;

    @Override
    public void start(Stage primaryStage) {
        loginController = new LoginController();

        // VBox layout for a consistent design with other views
        VBox mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #FFEB3B;"); // Consistent background color

        // Title
        Label title = new Label("Admin User Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // User management form
        GridPane userForm = createUserForm();

        // User list and management actions
        VBox userListSection = createUserListSection();

        // Back button
        Button backButton = createStyledButton("Back to Main Menu", "#E74C3C", "#C0392B");
        backButton.setOnAction(event -> {
            primaryStage.close(); // Close the admin management window
            AdminView adminView = new AdminView(primaryStage);
            adminView.show();
        });

        // Add components to the main layout
        mainLayout.getChildren().addAll(title, userForm, userListSection, backButton);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 600, 500);
        primaryStage.setTitle("Car Rental System - Admin User Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createUserForm() {
        GridPane userForm = new GridPane();
        userForm.setHgap(10);
        userForm.setVgap(10);
        userForm.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<User.Role> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll(User.Role.ADMIN, User.Role.CLERK, User.Role.CUSTOMER);

        userForm.add(new Label("Username:"), 0, 0);
        userForm.add(usernameField, 1, 0);
        userForm.add(new Label("Password:"), 0, 1);
        userForm.add(passwordField, 1, 1);
        userForm.add(new Label("Role:"), 0, 2);
        userForm.add(roleDropdown, 1, 2);

        Button addUserButton = createStyledButton("Add User", "#2ECC71", "#27AE60");
        Label messageLabel = new Label();
        userForm.add(addUserButton, 1, 3);
        userForm.add(messageLabel, 1, 4);

        addUserButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User.Role role = roleDropdown.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                messageLabel.setText("Please fill out all fields.");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            loginController.addUser(username, password, role);
            messageLabel.setText("User added successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
            
            usernameField.clear();
            passwordField.clear();
            roleDropdown.setValue(null);
        });

        return userForm;
    }

    private VBox createUserListSection() {
        VBox userListSection = new VBox(30);
        userListSection.setAlignment(Pos.CENTER);
        
        Label userListLabel = new Label("All Users:");
        userListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        userListSection.getChildren().add(userListLabel);

        ListView<String> userListView = new ListView<>();
        updateUserList(userListView);

        HBox userActions = new HBox(10);
        userActions.setAlignment(Pos.CENTER);
        Button deleteUserButton = createStyledButton("Delete User", "#F39C12", "#E67E22");
        userActions.getChildren().add(deleteUserButton);

        deleteUserButton.setOnAction(event -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser == null || selectedUser.startsWith("admin")) {
                return;
            }

            String username = selectedUser.split(" - ")[0];
            loginController.removeUser(username);
            updateUserList(userListView);
        });

        userListSection.getChildren().addAll(userListView, userActions);
        return userListSection;
    }

    private void updateUserList(ListView<String> userListView) {
        ArrayList<String> userStrings = new ArrayList<>();
        loginController.getAllUsers().forEach((username, userRole) -> {
            userStrings.add(username + " - " + userRole.getRole());
        });
        userListView.getItems().setAll(userStrings);
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-font-size: 16px; "
                + "-fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor + ";"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + baseColor + ";"));
        return button;
    }
}
