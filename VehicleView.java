package view;

import controller.VehicleController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Vehicle;

import java.util.ArrayList;

public class VehicleView extends Application {

    private VehicleController vehicleController;

    private static final String BG_COLOR = "-fx-background-color: #FFEB3B;";  // Bright yellow background
    private static final String BUTTON_STYLE = "-fx-background-color: #D32F2F; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;"; // Red button
    private static final String BUTTON_HOVER = "-fx-background-color: #C2185B; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;";  // Dark red hover effect

    @Override
    public void start(Stage primaryStage) {
        vehicleController = new VehicleController();

        // VBox layout for main container
        VBox mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(40));  // More padding for a spacious layout
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(BG_COLOR);

        // Title with updated styling
        Label title = new Label("Car Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // Vehicle management form
        GridPane vehicleForm = createVehicleForm();

        // Vehicle list and management actions
        VBox vehicleListSection = createVehicleListSection();

        // Back button
        Button backButton = createStyledButton("Back to Main Menu", "#E74C3C", "#C0392B");
        backButton.setOnAction(event -> {
            primaryStage.close();
            AdminView adminView = new AdminView(primaryStage);
            adminView.show();
        });

        // Add components to the main layout
        mainLayout.getChildren().addAll(title, vehicleForm, vehicleListSection, backButton);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 750, 650); // Increased window size for consistency
        primaryStage.setTitle("Car Rental System - Car Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createVehicleForm() {
        // Form layout
        GridPane vehicleForm = new GridPane();
        vehicleForm.setHgap(10);
        vehicleForm.setVgap(10);
        vehicleForm.setAlignment(Pos.CENTER);

        // Form fields
        TextField categoryField = new TextField();
        TextField modelField = new TextField();
        TextField manufacturerField = new TextField();
        TextField mileageField = new TextField();
        TextField dailyRateField = new TextField();
        TextField engineSizeField = new TextField();

        // Labels and fields
        vehicleForm.add(new Label("Category:"), 0, 0);
        vehicleForm.add(categoryField, 1, 0);
        vehicleForm.add(new Label("Model:"), 0, 1);
        vehicleForm.add(modelField, 1, 1);
        vehicleForm.add(new Label("Manufacturer:"), 0, 2);
        vehicleForm.add(manufacturerField, 1, 2);
        vehicleForm.add(new Label("Mileage:"), 0, 3);
        vehicleForm.add(mileageField, 1, 3);
        vehicleForm.add(new Label("Daily Rate:"), 0, 4);
        vehicleForm.add(dailyRateField, 1, 4);
        vehicleForm.add(new Label("Engine Size:"), 0, 5);
        vehicleForm.add(engineSizeField, 1, 5);

        // Add vehicle button
        Button addVehicleButton = createStyledButton("Add Vehicle", "#2ECC71", "#27AE60");
        Label messageLabel = new Label();
        vehicleForm.add(addVehicleButton, 1, 6);
        vehicleForm.add(messageLabel, 1, 7);

        // Add vehicle action
        addVehicleButton.setOnAction(event -> {
            try {
                String category = categoryField.getText();
                String model = modelField.getText();
                String manufacturer = manufacturerField.getText();
                double mileage = Double.parseDouble(mileageField.getText());
                double dailyRate = Double.parseDouble(dailyRateField.getText());
                double engineSize = Double.parseDouble(engineSizeField.getText());

                vehicleController.registerVehicle(new Vehicle(category, model, manufacturer, mileage, dailyRate, engineSize));
                messageLabel.setText("Vehicle added successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");

                // Clear fields
                categoryField.clear();
                modelField.clear();
                manufacturerField.clear();
                mileageField.clear();
                dailyRateField.clear();
                engineSizeField.clear();
            } catch (NumberFormatException e) {
                messageLabel.setText("Invalid input. Please check the values.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        return vehicleForm;
    }

    private VBox createVehicleListSection() {
        // Vehicle list section layout
        VBox vehicleListSection = new VBox(30);
        vehicleListSection.setAlignment(Pos.CENTER);

        // Vehicle list label
        Label vehicleListLabel = new Label("All Vehicles:");
        vehicleListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        vehicleListSection.getChildren().add(vehicleListLabel);

        // Vehicle list view
        ListView<String> vehicleListView = new ListView<>();
        updateVehicleList(vehicleListView);

        // Buttons for vehicle actions
        HBox vehicleActions = new HBox(15);
        vehicleActions.setAlignment(Pos.CENTER);
        Button deleteVehicleButton = createStyledButton("Delete Vehicle", "#F39C12", "#E67E22");
        vehicleActions.getChildren().add(deleteVehicleButton);

        // Delete vehicle action
        deleteVehicleButton.setOnAction(event -> {
            String selectedVehicle = vehicleListView.getSelectionModel().getSelectedItem();
            if (selectedVehicle == null) return;

            // Extract model
            String model = selectedVehicle.split(" - ")[1];
            vehicleController.removeVehicle(model);
            updateVehicleList(vehicleListView);
        });

        // Add components to the vehicle list section
        vehicleListSection.getChildren().addAll(vehicleListView, vehicleActions);

        return vehicleListSection;
    }

    private void updateVehicleList(ListView<String> vehicleListView) {
        ArrayList<String> vehicleStrings = new ArrayList<>();
        vehicleController.listAllVehicles().forEach(vehicle -> {
            vehicleStrings.add(vehicle.getCategory() + " - " + vehicle.getModel() + " - " + vehicle.getManufacturer());
        });
        vehicleListView.getItems().setAll(vehicleStrings);
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        return button;
    }

}
