import java.io.*;
import java.util.*;

/**
 * A utility class that handles user data storage and validation.
 * User credentials are stored in a simple text file named "users.txt",
 * where each line follows the format: username:password
 */
public class UserDatabase {
    /** The file path where user credentials are stored. */
    private static final String USER_FILE = "users.txt";

    /**
     * Adds a new user to the database if the username does not already exist.
     *
     * @param username The desired username
     * @param password The password associated with the username
     * @return true if the user was successfully added; false if the user already exists or an I/O error occurred
     */
    public static boolean addUser(String username, String password) {
        if (userExists(username)) return false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("File doesn't exist");
            return false;
        }
    }

    /**
     * Validates if the provided username and password match an existing user.
     *
     * @param username The username to check
     * @param password The password to verify
     * @return true if the credentials are valid; false otherwise or if an I/O error occurs
     */
    public static boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("File doesn't exist");
        }
        return false;
    }

    /**
     * Checks if a user with the given username already exists in the database.
     *
     * @param username The username to check
     * @return true if the username exists; false otherwise or if an I/O error occurs
     */
    private static boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("File doesn't exist");
        }
        return false;
    }
}
