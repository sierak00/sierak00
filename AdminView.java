package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class AdminView {
    private Stage primaryStage;
    private static final String BG_COLOR = "-fx-background-color: #FFEB3B;";  // Bright yellow background
    private static final String BUTTON_STYLE = "-fx-background-color: #D32F2F; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;"; // Red button
    private static final String BUTTON_HOVER = "-fx-background-color: #C2185B; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;";  // Dark red hover effect
    
    public AdminView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox layout = new VBox(30); // Increased space between elements
        layout.setPadding(new Insets(40)); // Increased padding for more spacious layout
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(BG_COLOR);

        // Admin label with larger font size
        Label adminLabel = new Label("Welcome, Admin!");
        adminLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // Buttons with updated styling
        Button manageUsersButton = createStyledButton("Manage Users", e -> handleManageUsers());
        Button manageClerksButton = createStyledButton("Manage Clerks", e -> handleManageClerks());

        // Add buttons to layout
        layout.getChildren().addAll(adminLabel, manageUsersButton, manageClerksButton);

        // Set scene with larger size
        Scene scene = new Scene(layout, 750, 650); // Increased window size
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(event -> button.setStyle(BUTTON_HOVER));
        button.setOnMouseExited(event -> button.setStyle(BUTTON_STYLE));
        button.setOnAction(handler);
        return button;
    }

    private void handleManageUsers() {
        UserView userManagementView = new UserView();
        userManagementView.start(primaryStage);
    }

    private void handleManageClerks() {
        ClerkView clerkManagementView = new ClerkView();
        clerkManagementView.start(primaryStage);
    }
}
