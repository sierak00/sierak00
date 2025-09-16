package controller;

import model.Client;
import model.Handler;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.ListView;

public class ClientController {
    private final String CUSTOMER_DATA_FILE = "data/customers.ser";
    private ArrayList<Client> customers;

    public ClientController() {
        try {
            this.customers = Handler.loadData(CUSTOMER_DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            this.customers = new ArrayList<>();
            System.err.println("Error loading customer data: " + e.getMessage());
        }
    }

    // Add a new customer
    public void addCustomer(Client customer) {
        customers.add(customer);
        saveData();
    }

    // Search for customers by name
    public Client searchCustomer(String name, String surname) {
        for (Client customer : customers) {
            if (customer.getName().equalsIgnoreCase(name) &&
                    customer.getSurname().equalsIgnoreCase(surname)) {
                return customer;
            }
        }
        return null;
    }

    // Save data to file
    private void saveData() {
        try {
            Handler.saveData(CUSTOMER_DATA_FILE, customers);
        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
        }
    }

    // Get all customers
    public ArrayList<Client> getAllCustomers() {
        return customers;
    }
    
    // Method to get selected customer based on ListView selection
    public Client getSelectedCustomer(ListView<String> searchResults) {
        String selectedCustomerStr = searchResults.getSelectionModel().getSelectedItem();
        if (selectedCustomerStr != null) {
            // Assuming you need to parse the customer info back to a Customer object
            // This should be implemented according to your actual search logic
            for (Client customer : customers) { // 'customers' is the list you have in the controller
                if (selectedCustomerStr.contains(customer.getName())) {
                    return customer;
                }
            }
        }
        return null;
    }
}
