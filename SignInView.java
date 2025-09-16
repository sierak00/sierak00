package view;

import Main.StartApp;
import controller.LoginController;
import model.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignInView extends Application {

    private LoginController loginController;
    private StartApp mainApp; // Reference to MainApp for navigation

    public SignInView() {
        this.mainApp = new StartApp();
    }

    @Override
    public void start(Stage primaryStage) {
        loginController = new LoginController();

        // VBox layout for the main container
        VBox mainLayout = new VBox(30); // Increased space between elements
        mainLayout.setPadding(new Insets(40)); // Increased padding
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #FFEB3B;"); // Bright yellow background

        // Title
        Label title = new Label("Car Rental System");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // Create login form
        GridPane loginForm = createLoginForm(primaryStage);

        // Add elements to the layout
        mainLayout.getChildren().addAll(title, loginForm);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 750, 650); // Increased window size
        primaryStage.setTitle("Car Rental System - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createLoginForm(Stage primaryStage) {
        // GridPane layout for the form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30)); // Increased padding
        grid.setHgap(25); // More horizontal space
        grid.setVgap(25); // More vertical space
        grid.setAlignment(Pos.CENTER);

        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        styleTextField(usernameField);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        // Password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        styleTextField(passwordField);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        // Login button
        Button loginButton = createStyledButton("SignIn");
        grid.add(loginButton, 1, 2);

        // Message label for feedback
        Label messageLabel = new Label();
        grid.add(messageLabel, 1, 3);

        // Handle login button click
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText(), messageLabel, primaryStage));

        return grid;
    }

    private void handleLogin(String username, String password, Label messageLabel, Stage primaryStage) {
        User.Role role = loginController.validateLogin(username, password);

        if (role != null) {
            messageLabel.setText("Login successful!");
            messageLabel.setStyle("-fx-text-fill: green;");

            // Redirect based on role
            switch (role) {
                case ADMIN:
                    mainApp.showAdminView(primaryStage);
                    break;
                case CLERK:
                    mainApp.showClerkView(primaryStage);
                    break;
                case CUSTOMER:
                    mainApp.showCustomerView(primaryStage);
                    break;
            }
        } else {
            messageLabel.setText("Invalid credentials. Try again.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #B71C1C; -fx-border-radius: 8px; -fx-padding: 12px; -fx-font-size: 16px;");
        textField.setStyle("-fx-focus-color: #FF5722;"); // Focus color is vibrant orange
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #C2185B; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        return button;
    }
}
