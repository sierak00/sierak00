package controller;

import model.Vehicle;
import model.Client;
import model.Handler;
import model.Booking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleController {
    private final String vehicleDataPath = "data/cars.ser";
    private final String reservationDataPath = "data/bookings.ser"; 
    private ArrayList<Vehicle> vehicleList;
    private ArrayList<Booking> reservationList;

    public VehicleController() {
        try {
            vehicleList = Handler.loadData(vehicleDataPath);
            reservationList = Handler.loadData(reservationDataPath);
        } catch (IOException | ClassNotFoundException ex) {
            vehicleList = new ArrayList<>();
            reservationList = new ArrayList<>();
        }
    }

    public void registerVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
        persistVehicleData();
    }

    public ArrayList<Vehicle> findVehicles(String category, boolean isAvailable) {
        ArrayList<Vehicle> matchedVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getCategory().equalsIgnoreCase(category) && vehicle.isAvailable() == isAvailable) {
                matchedVehicles.add(vehicle);
            }
        }
        return matchedVehicles;
    }

    public void changeVehicleAvailability(Vehicle vehicle, boolean isAvailable) {
        vehicle.setAvailable(isAvailable);
        persistVehicleData();
    }

    public List<Vehicle> listAllVehicles() {
        return vehicleList;
    }

    public void modifyVehicleDetails(Vehicle vehicle, String newCategory, String newModel, double updatedDailyRate) {
        vehicle.setCategory(newCategory);
        vehicle.setModel(newModel);
        vehicle.setDailyRate(updatedDailyRate);
        persistVehicleData();
    }

    public void bookVehicle(Client customer, Vehicle vehicle, int rentalDuration) {
        Booking newReservation = new Booking(customer.getName() + " " + customer.getSurname(), vehicle, rentalDuration);
        reservationList.add(newReservation);
        persistReservationData();
    }

    public double computeReservationCost(Booking reservation, double taxRate) {
        double basePrice = reservation.getCar().getDailyRate() * reservation.getRentalDays();
        double taxAmount = basePrice * taxRate;
        return basePrice + taxAmount;
    }

    public String createInvoice(Booking reservation, double taxRate) {
        double finalCost = computeReservationCost(reservation, taxRate);
        return "Invoice:\n" +
                "Client: " + reservation.getCustomerName() + "\n" +
                "Vehicle Model: " + reservation.getCar().getModel() + "\n" +
                "Rental Period: " + reservation.getRentalDays() + "\n" +
                "Total Amount (including tax): " + finalCost + "\n";
    }

    private void persistVehicleData() {
        try {
            Handler.saveData(vehicleDataPath, vehicleList);
        } catch (IOException ex) {
            System.err.println("Failed to store vehicle data: " + ex.getMessage());
        }
    }

    private void persistReservationData() {
        try {
            Handler.saveData(reservationDataPath, reservationList);
        } catch (IOException ex) {
            System.err.println("Failed to store reservation data: " + ex.getMessage());
        }
    }
    
    public Vehicle fetchVehicleByCategory(String category) {
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getCategory().equalsIgnoreCase(category)) {
                return vehicle;
            }
        }
        return null;
    }

    public void removeVehicle(String model) {
        vehicleList.removeIf(vehicle -> vehicle.getModel().equalsIgnoreCase(model));
        persistVehicleData();
    }
}
