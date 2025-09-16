package view;

import controller.BookingController;
import controller.VehicleController;
import controller.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import model.Booking;

import java.util.ArrayList;

public class BookingView {
    private BookingController bookingController;
    private ClientController clientController;
    private VehicleController vehicleController;
    private Runnable onBack;

    public BookingView() {
        bookingController = new BookingController();
        clientController = new ClientController();
        vehicleController = new VehicleController();
    }

    public void show(Stage stage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #e0f7fa;");

        // Title
        Label title = new Label("Booking Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Buttons for various actions
        Button viewBookingsButton = createStyledButton("View All Bookings", "#D32F2F", "#C2185B");
        Button editBookingButton = createStyledButton("Edit Booking", "#D32F2F", "#C2185B");
        Button cancelBookingButton = createStyledButton("Cancel Booking", "#D32F2F", "#C2185B");
        Button backButton = createStyledButton("Back to Main Menu", "#E74C3C", "#C0392B");

        // Button Actions
        viewBookingsButton.setOnAction(event -> openViewBookingsWindow());
        editBookingButton.setOnAction(event -> openEditBookingWindow());
        cancelBookingButton.setOnAction(event -> openCancelBookingWindow());
        backButton.setOnAction(event -> { if (onBack != null) onBack.run(); });

        mainLayout.getChildren().addAll(title, viewBookingsButton, editBookingButton, cancelBookingButton, backButton);

        Scene scene = new Scene(mainLayout, 750, 650);
        stage.setTitle("Car Rental System - Manage Bookings");
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-font-size: 16px; " +
                "-fx-padding: 12px 25px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor + ";"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + baseColor + ";"));
        return button;
    }

    private void openViewBookingsWindow() {
        Stage viewBookingsStage = new Stage();
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #e0f7fa;");

        Label title = new Label("All Bookings");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        ListView<Booking> bookingsList = new ListView<>();
        ArrayList<Booking> bookings = (ArrayList<Booking>) bookingController.fetchAllBookings();
        bookingsList.getItems().addAll(bookings);

        Button closeButton = createStyledButton("Close", "#E74C3C", "#C0392B");
        closeButton.setOnAction(event -> viewBookingsStage.close());

        layout.getChildren().addAll(title, bookingsList, closeButton);

        Scene scene = new Scene(layout, 850, 450);
        viewBookingsStage.setScene(scene);
        viewBookingsStage.setTitle("View All Bookings");
        viewBookingsStage.show();
    }

    private void openEditBookingWindow() {
        Stage editBookingStage = new Stage();
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #e0f7fa;");

        Label title = new Label("Edit Booking");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        ComboBox<Booking> bookingsDropdown = new ComboBox<>();
        bookingsDropdown.getItems().addAll(bookingController.fetchAllBookings());

        TextField rentalDaysField = new TextField();
        rentalDaysField.setPromptText("New Rental Days");

        Button updateButton = createStyledButton("Update Booking", "#2ECC71", "#27AE60");
        Label messageLabel = new Label();

        updateButton.setOnAction(event -> {
            Booking selectedBooking = bookingsDropdown.getValue();
            try {
                int newRentalDays = Integer.parseInt(rentalDaysField.getText());
                bookingController.editBooking(selectedBooking, newRentalDays);
                messageLabel.setText("Booking updated successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
            } catch (Exception e) {
                messageLabel.setText("Failed to update booking. Please check input.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        layout.getChildren().addAll(title, bookingsDropdown, rentalDaysField, updateButton, messageLabel);

        Scene scene = new Scene(layout, 850, 400);
        editBookingStage.setScene(scene);
        editBookingStage.setTitle("Edit Booking");
        editBookingStage.show();
    }

    private void openCancelBookingWindow() {
        Stage cancelBookingStage = new Stage();
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #e0f7fa;");

        Label title = new Label("Cancel Booking");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        ComboBox<Booking> bookingsDropdown = new ComboBox<>();
        bookingsDropdown.getItems().addAll(bookingController.fetchAllBookings());

        Button cancelButton = createStyledButton("Cancel Booking", "#F39C12", "#E67E22");
        Label messageLabel = new Label();

        cancelButton.setOnAction(event -> {
            Booking selectedBooking = bookingsDropdown.getValue();
            if (selectedBooking != null) {
                bookingController.cancelBooking(selectedBooking);
                bookingsDropdown.getItems().remove(selectedBooking);
                messageLabel.setText("Booking canceled successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("No booking selected.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        layout.getChildren().addAll(title, bookingsDropdown, cancelButton, messageLabel);

        Scene scene = new Scene(layout, 850, 400);
        cancelBookingStage.setScene(scene);
        cancelBookingStage.setTitle("Cancel Booking");
        cancelBookingStage.show();
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
