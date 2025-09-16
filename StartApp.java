package Main;

import java.util.ArrayList;
import java.util.List;

import controller.BookingController;
import controller.VehicleController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Vehicle;
import view.*;

public class StartApp extends Application {

    private Stage primaryStage; 
    																											
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginView();
    }

    /**
     * Display the login view.
     */
    private void showLoginView() {
        SignInView loginView = new SignInView();
        //loginView.setMainApp(this); // Pass MainApp reference to LoginView
        loginView.start(primaryStage);
    }

    /**
     * Display the admin view.
     */
    public void showAdminView(Stage primaryStage) {
        AdminView adminView = new AdminView(primaryStage); // Pass the primary stage to AdminView
        adminView.show();
    }

    /**
     * Display the clerk view.
     */
    public void showClerkView(Stage primaryStage) {
        MenuView mainMenuView = new MenuView();
        extracted(mainMenuView);
        mainMenuView.setOnCustomerManagement(() -> showCustomerManagementView(primaryStage));
        mainMenuView.setOnBookingManagement(() -> showBookingManagementView(primaryStage));
        mainMenuView.start(primaryStage);
    }

	private void extracted(MenuView mainMenuView) {
		mainMenuView.setOnCarManagement(() -> showCarManagementView(primaryStage));
	}

    /**
     * Display the customer view.
     */
    public void showCustomerView(Stage primaryStage) {
        VehicleController cc = new VehicleController();
        List<Vehicle> cars = cc.listAllVehicles();
        BookingController bc = new BookingController();
        ClientBookingView customerBookingView = new ClientBookingView(cars, bc);
        //customerBookingView.setOnBack(() -> showLoginView());
        customerBookingView.start(primaryStage);
    }

    /**
     * Display the car management view (clerk functionality).
     */
    private void showCarManagementView(Stage primaryStage) {
        VehicleView carManagementView = new VehicleView();
        //carManagementView.setOnBack(() -> showClerkView(primaryStage));
        carManagementView.start(primaryStage);
    }

    /**
     * Display the customer management view (clerk functionality).
     */
    private void showCustomerManagementView(Stage primaryStage) {
        ClientView customerManagementView = new ClientView();
        customerManagementView.setOnBack(() -> showClerkView(primaryStage));
        customerManagementView.start(primaryStage);
    }

    /**
     * Display the booking management view (clerk functionality).
     */
    private void showBookingManagementView(Stage primaryStage) {
        BookingView bookingView = new BookingView();
        bookingView.setOnBack(() -> showClerkView(primaryStage));
        bookingView.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
