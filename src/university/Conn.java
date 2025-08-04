package university;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conn {
    private final String loginFile = "login.txt";
    private static final Logger LOGGER = Logger.getLogger(Conn.class.getName());

    // Example: Check login credentials from file
    public boolean checkLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(loginFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File error: " + e.getMessage(), e);
        }
        return false;
    }

    // Example: Add new login credentials
    public boolean addLogin(String username, String password) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loginFile, true))) {
            bw.write(username + "," + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File error: " + e.getMessage(), e);
            return false;
        }
    }

    // You can add similar methods for students, teachers, marks, etc.
}
