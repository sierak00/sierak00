package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView extends Application {

    private Runnable onCarManagement;
    private Runnable onCustomerManagement;
    private Runnable onBookingManagement;

    private static final String BG_COLOR = "-fx-background-color: #FFEB3B;";  // Bright yellow background
    private static final String BUTTON_STYLE = "-fx-background-color: #D32F2F; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;"; // Red button
    private static final String BUTTON_HOVER = "-fx-background-color: #C2185B; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;";  // Dark red hover effect

    @Override
    public void start(Stage primaryStage) {
        // VBox layout for a consistent design
        VBox mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(40));  // More padding for a spacious layout
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(BG_COLOR);

        // Title with updated styling
        Label title = new Label("Main Menu");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // Menu buttons with updated button styling
        Button carManagementButton = createStyledButton("Manage Cars", e -> {
            if (onCarManagement != null) onCarManagement.run();
        });
        Button customerManagementButton = createStyledButton("Manage Customers", e -> {
            if (onCustomerManagement != null) onCustomerManagement.run();
        });
        Button bookingManagementButton = createStyledButton("Manage Bookings", e -> {
            if (onBookingManagement != null) onBookingManagement.run();
        });

        // Adding elements to the layout
        mainLayout.getChildren().addAll(title, carManagementButton, customerManagementButton, bookingManagementButton);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 750, 650); // Increased window size for consistency
        primaryStage.setTitle("Car Rental System - Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to create buttons with unified style
    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(event -> button.setStyle(BUTTON_HOVER));
        button.setOnMouseExited(event -> button.setStyle(BUTTON_STYLE));
        button.setOnAction(handler);
        return button;
    }

    // Setters for callback methods
    public void setOnCarManagement(Runnable onCarManagement) {
        this.onCarManagement = onCarManagement;
    }

    public void setOnCustomerManagement(Runnable onCustomerManagement) {
        this.onCustomerManagement = onCustomerManagement;
    }

    public void setOnBookingManagement(Runnable onBookingManagement) {
        this.onBookingManagement = onBookingManagement;
    }

}
