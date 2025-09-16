package view;

import controller.ClientController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Client;

import java.util.ArrayList;

public class ClientView extends Application {
    private Runnable onBack; // Callback për butonin "Back"

    private ClientController customerController;

    private static final String BG_COLOR = "-fx-background-color: #FFEB3B;";
    private static final String BUTTON_STYLE = "-fx-background-color: #D32F2F; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;";
    private static final String BUTTON_HOVER = "-fx-background-color: #C2185B; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;";

    @Override
    public void start(Stage primaryStage) {
        customerController = new ClientController();

        VBox mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(BG_COLOR);

        Label title = new Label("Customer Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #D32F2F;");

        GridPane addCustomerForm = createAddCustomerForm();
        VBox searchSection = createSearchSection();
        Button backButton = createStyledButton("Back to Main Menu", e -> {
            if (onBack != null) onBack.run();
        });

        mainLayout.getChildren().addAll(title, addCustomerForm, searchSection, backButton);

        primaryStage.setScene(new Scene(mainLayout, 600, 600));
        primaryStage.setTitle("Car Rental System - Manage Customers");
        primaryStage.show();
    }

    private GridPane createAddCustomerForm() {
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField genderField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();

        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Surname:"), surnameField);
        form.addRow(2, new Label("Gender:"), genderField);
        form.addRow(3, new Label("Email:"), emailField);
        form.addRow(4, new Label("Phone:"), phoneField);
        form.addRow(5, new Label("Address:"), addressField);

        Button addButton = createStyledButton("Add Customer", e -> handleAddCustomer(nameField, surnameField, genderField, emailField, phoneField, addressField));

        Label messageLabel = new Label();
        form.addRow(6, addButton);
        form.addRow(7, messageLabel);

        return form;
    }

    private void handleAddCustomer(TextField name, TextField surname, TextField gender, TextField email, TextField phone, TextField address) {
        try {
            Client customer = new Client(name.getText(), surname.getText(), gender.getText(), email.getText(), phone.getText(), address.getText());
            customerController.addCustomer(customer);
            showAlert("Customer added successfully!", Alert.AlertType.INFORMATION);
            name.clear(); surname.clear(); gender.clear(); email.clear(); phone.clear(); address.clear();
        } catch (Exception e) {
            showAlert("Failed to add customer. Try again.", Alert.AlertType.ERROR);
        }
    }

    private VBox createSearchSection() {
        VBox searchSection = new VBox(10);
        HBox searchControls = new HBox(10);
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        Button searchButton = createStyledButton("Search", e -> handleSearchCustomer(nameField, surnameField));
        ListView<String> searchResults = new ListView<>();

        nameField.setPromptText("Name");
        surnameField.setPromptText("Surname");
        searchControls.getChildren().addAll(nameField, surnameField, searchButton);

        Button viewAllButton = createStyledButton("View All Customers", e -> handleViewAllCustomers(searchResults));

        searchSection.getChildren().addAll(new Label("Search Customers"), searchControls, searchResults, viewAllButton);
        return searchSection;
    }

    private void handleSearchCustomer(TextField name, TextField surname) {
        Client customer = customerController.searchCustomer(name.getText(), surname.getText());
        showSearchResults(customer == null ? "No customer found." : customer.toString());
    }

    private void handleViewAllCustomers(ListView<String> results) {
        ArrayList<Client> customers = customerController.getAllCustomers();
        results.getItems().clear();
        if (customers.isEmpty()) {
            results.getItems().add("No customers found.");
        } else {
            for (Client customer : customers) {
                results.getItems().add(customer.toString());
            }
        }
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(event -> button.setStyle(BUTTON_HOVER));
        button.setOnMouseExited(event -> button.setStyle(BUTTON_STYLE));
        button.setOnAction(handler);
        return button;
    }

    private void showAlert(String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSearchResults(String message) {
        // Update the ListView with the search result
        ListView<String> results = new ListView<>();
        results.getItems().clear();
        results.getItems().add(message);
    }

    // Method to set the callback for the "Back" button
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
