package controller;

import model.User;
import model.User.Role;

import java.io.*;
import java.util.HashMap;

public class LoginController {

    private final String USER_DATA_FILE = "data/users.ser";
    private HashMap<String, User> users;

    public LoginController() {
        try {
            // Load users from file
            users = loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            users = new HashMap<>();
            System.err.println("Error loading user data: " + e.getMessage());
        }

        // Ensure default users exist
        addUser("admin", "admin", User.Role.ADMIN);
        addUser("clerk", "clerk123", User.Role.CLERK);
        addUser("customer", "customer123", User.Role.CUSTOMER);
    }

    /**
     * Validates the username and password.
     * If valid, returns the user's role; otherwise, returns null.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return The Role of the user if credentials are valid, or null if invalid.
     */
    public User.Role validateLogin(String username, String password) {
        User user = users.get(username);

        if (user != null && user.getPassword().equals(password)) {
            return user.getRole();
        }
        return null; // Invalid credentials
    }

    /**
     * Adds a new user with the given username, password, and role.
     * Prevents duplicate usernames.
     */
    public void addUser(String username, String password, User.Role role) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password, role));
            saveUsers();
            System.out.println("User added: " + username + " with role: " + role);
        }
    }

    /**
     * Loads users from the serialized file.
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, User> loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, User>) ois.readObject();
        }
    }

    /**
     * Saves users to the serialized file.
     */
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    /**
     * Get all users.
     */
    public HashMap<String, User> getAllUsers() {
        return users;
    }

    /**
     * Removes a user by username.
     */
    public void removeUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            saveUsers();
            System.out.println("User removed: " + username);
        }
    }
}
