package controller;

import model.Clerk;
import java.io.*;
import java.util.ArrayList;

public class ClerkController {
    private ArrayList<Clerk> clerks;
    private final String FILE_NAME = "data/clerks.ser"; // Emri i skedarit ku do t� ruhen t� dh�nat

    // Constructor
    public ClerkController() {
        this.clerks = new ArrayList<>();
        loadClerksFromFile();  // Pasi t� krijohet objekti, ngarko t� dh�nat nga file
    }

    // Add a new Clerk and save to file
    public void addClerk(Clerk clerk) {
        clerks.add(clerk);
        saveClerksToFile();  // Pasi t� shtohet nj� Clerk, ruaj t� dh�nat n� file
    }

    // Search for a Clerk by name and role
    public Clerk searchClerk(String name, String role) {
        for (Clerk clerk : clerks) {
            if (clerk.getName().equalsIgnoreCase(name) && clerk.getRole().equalsIgnoreCase(role)) {
                return clerk;
            }
        }
        return null; // Return null if no match is found
    }

    // Get all Clerks
    public ArrayList<Clerk> getAllClerks() {
        return clerks;
    }

    // Ruaj t� dh�nat n� file
    private void saveClerksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(clerks);  // Shkruaj list�n e clerks n� file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClerksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            clerks = (ArrayList<Clerk>) ois.readObject();  // Try to read the list of clerks
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, do nothing (it's okay to start with an empty list)
        } catch (EOFException e) {
            // If end of file is reached, it's likely that the file was empty or truncated; we can ignore
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
