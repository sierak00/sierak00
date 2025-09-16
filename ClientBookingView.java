package view;

import controller.BookingController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Vehicle;

import java.util.List;

public class ClientBookingView extends Application {
    private Runnable onBack; // Callback për butonin "Back"
    private static final String BG_COLOR = "-fx-background-color: #f5f5f5;";
    private static final String BUTTON_STYLE = "-fx-background-color: #3498db; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;";
    private static final String BUTTON_HOVER = "-fx-background-color: #2980b9; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;";

    private TableView<Vehicle> carTable;
    private ObservableList<Vehicle> carData;
    private BookingController bookingController;
    private List<Vehicle> cars;
    private Stage primaryStage;

    public ClientBookingView(List<Vehicle> cars, BookingController bookingController) {
        this.cars = cars;
        this.bookingController = bookingController;
        this.carData = FXCollections.observableArrayList(cars);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Car Booking System");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(BG_COLOR);

        Label titleLabel = new Label("Car Booking System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        carTable = new TableView<>();
        carTable.setItems(carData);
        carTable.getColumns().addAll(
                createColumn("Category", Vehicle::getCategory),
                createColumn("Model", Vehicle::getModel),
                createColumn("Manufacturer", Vehicle::getManufacturer),
                createColumn("Mileage", v -> String.valueOf(v.getMileage())),
                createColumn("Daily Rate", v -> String.valueOf(v.getDailyRate())),
                createColumn("Engine Size", v -> String.valueOf(v.getEngineSize())),
                createColumn("Available", v -> v.isAvailable() ? "Yes" : "No")
        );

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button searchButton = createStyledButton("Search", e -> handleSearch());
        Button bookButton = createStyledButton("Book", e -> handleBook());
        Button updateButton = createStyledButton("Update", e -> handleUpdate());
        Button deleteButton = createStyledButton("Delete", e -> handleDelete());

        buttonBox.getChildren().addAll(searchButton, bookButton, updateButton, deleteButton);

        layout.getChildren().addAll(titleLabel, carTable, buttonBox);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableColumn<Vehicle, String> createColumn(String title, java.util.function.Function<Vehicle, String> mapper) {
        TableColumn<Vehicle, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(mapper.apply(data.getValue())));
        return column;
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(event -> button.setStyle(BUTTON_HOVER));
        button.setOnMouseExited(event -> button.setStyle(BUTTON_STYLE));
        button.setOnAction(handler);
        return button;
    }

    private void handleSearch() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Car");
        dialog.setContentText("Enter car model or manufacturer to search:");
        dialog.showAndWait().ifPresent(this::searchCars);
    }

    private void handleBook() {
        Vehicle selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null || !selectedCar.isAvailable()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an available car to book.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Success", "Car booked successfully!");
    }

    private void handleUpdate() {
        Vehicle selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a car to update.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Success", "Car updated successfully!");
    }

    private void handleDelete() {
        Vehicle selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a car to delete.");
            return;
        }
        carData.remove(selectedCar);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Car deleted successfully!");
    }

    private void searchCars(String searchTerm) {
        ObservableList<Vehicle> filteredCars = FXCollections.observableArrayList();
        for (Vehicle car : cars) {
            if (car.getModel().equalsIgnoreCase(searchTerm) || car.getManufacturer().equalsIgnoreCase(searchTerm)) {
                filteredCars.add(car);
            }
        }
        carTable.setItems(filteredCars);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Metoda për të vendosur callback për butonin "Back"
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
