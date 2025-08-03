package university;

import java.io.*;

public class Conn {
    private final String loginFile = "login.txt";

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
            System.out.println("File error: " + e.getMessage());
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
            System.out.println("File error: " + e.getMessage());
            return false;
        }
    }

    // You can add similar methods for students, teachers, marks, etc.
}
