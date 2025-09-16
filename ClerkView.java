package view;

import controller.ClerkController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Clerk;

import java.util.ArrayList;

public class ClerkView extends Application {

    private ClerkController clerkController;
    private static final String BG_COLOR = "-fx-background-color: #FFEB3B;";  // Bright yellow background
    private static final String BUTTON_STYLE = "-fx-background-color: #D32F2F; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;"; // Red button
    private static final String BUTTON_HOVER = "-fx-background-color: #C2185B; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;";  // Dark red hover effect

    @Override
    public void start(Stage primaryStage) {
        clerkController = new ClerkController();

        // VBox layout for main container
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(40));  // More padding for spacious layout
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(BG_COLOR);

        // Title with styling
        Label title = new Label("Clerk Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        // Add clerk form
        GridPane addClerkForm = createAddClerkForm();

        // Search and list clerks section
        VBox searchSection = createSearchSection();

        // Back button
        Button backButton = createStyledButton("Back to Admin Dashboard", event -> {
            primaryStage.close(); // Close the current view
            AdminView adminView = new AdminView(primaryStage); 
            adminView.show();
        });

        // Add components to the main layout
        mainLayout.getChildren().addAll(title, addClerkForm, searchSection, backButton);

        // Set up the scene and stage
        Scene scene = new Scene(mainLayout, 750, 650); // Larger window size
        primaryStage.setTitle("Clerk Management11");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createAddClerkForm() {
        // Form layout for adding a clerk
        GridPane addClerkForm = new GridPane();
        addClerkForm.setHgap(15);
        addClerkForm.setVgap(15);
        addClerkForm.setStyle("-fx-background-color: #ffffff; -fx-padding: 30px;");

        // Form fields
        TextField nameField = new TextField();
        TextField roleField = new TextField();
        TextField statusField = new TextField();

        // Labels
        addClerkForm.add(new Label("Name:"), 0, 0);
        addClerkForm.add(nameField, 1, 0);
        addClerkForm.add(new Label("Role:"), 0, 1);
        addClerkForm.add(roleField, 1, 1);
        addClerkForm.add(new Label("Status:"), 0, 2);
        addClerkForm.add(statusField, 1, 2);

        // Add clerk button
        Button addClerkButton = createStyledButton("Add Clerk", event -> {
            try {
                String name = nameField.getText();
                String role = roleField.getText();
                String status = statusField.getText();

                // Create and add clerk
                Clerk clerk = new Clerk(name, role, status);
                clerkController.addClerk(clerk);

                // Clear form
                nameField.clear();
                roleField.clear();
                statusField.clear();

                showAlert(Alert.AlertType.INFORMATION, "Clerk added successfully!", "Clerk has been added.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Failed to add clerk", "An error occurred while adding the clerk.");
            }
        });

        // Add components to form
        addClerkForm.add(addClerkButton, 1, 3);

        return addClerkForm;
    }

    private VBox createSearchSection() {
        // Search section layout
        VBox searchSection = new VBox(15);
        searchSection.setStyle("-fx-background-color: #ffffff; -fx-padding: 30px;");

        // Search controls
        HBox searchControls = new HBox(15);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField roleField = new TextField();
        roleField.setPromptText("Role");
        Button searchButton = createStyledButton("Search", event -> {
            String name = nameField.getText();
            String role = roleField.getText();

            Clerk clerk = clerkController.searchClerk(name, role);
            ListView<String> searchResults = new ListView<>();
            searchResults.getItems().clear();

            if (clerk == null) {
                searchResults.getItems().add("No clerk found.");
            } else {
                searchResults.getItems().add(clerk.toString());
            }
            searchSection.getChildren().add(searchResults);
        });

        searchControls.getChildren().addAll(nameField, roleField, searchButton);

        // View all clerks button
        Button viewAllButton = createStyledButton("View All Clerks", event -> {
            ArrayList<Clerk> clerks = clerkController.getAllClerks();
            ListView<String> searchResults = new ListView<>();
            searchResults.getItems().clear();

            if (clerks.isEmpty()) {
                searchResults.getItems().add("No clerks found.");
            } else {
                for (Clerk clerk : clerks) {
                    searchResults.getItems().add(clerk.toString());
                }
            }
            searchSection.getChildren().add(searchResults);
        });

        searchSection.getChildren().addAll(new Label("Search Clerks"), searchControls, viewAllButton);

        return searchSection;
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(event -> button.setStyle(BUTTON_HOVER));
        button.setOnMouseExited(event -> button.setStyle(BUTTON_STYLE));
        button.setOnAction(handler);
        return button;
    }

    // Display alert for actions
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
