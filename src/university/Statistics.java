package university;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class Statistics {
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #212F3D, #2C3E50);");

        Label title = new Label("University Statistics");
        title.setFont(Font.font("Segoe UI", 36));
        title.setTextFill(Color.WHITE);

        Label totalStudents = new Label();
        Label totalTeachers = new Label();
        Label totalFaculties = new Label();
        Label totalDepartments = new Label();
        totalStudents.setFont(Font.font("Segoe UI", 22));
        totalTeachers.setFont(Font.font("Segoe UI", 22));
        totalFaculties.setFont(Font.font("Segoe UI", 22));
        totalDepartments.setFont(Font.font("Segoe UI", 22));
        totalStudents.setTextFill(Color.WHITE);
        totalTeachers.setTextFill(Color.WHITE);
        totalFaculties.setTextFill(Color.WHITE);
        totalDepartments.setTextFill(Color.WHITE);

        try {
            // Count total students
            int studentCount = 0;
            java.util.Set<String> faculties = new java.util.HashSet<>();
            java.util.Set<String> departments = new java.util.HashSet<>();
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 13) {
                        studentCount++;
                        // Assuming faculty is at index 11 and department at index 12
                        faculties.add(parts[11]);
                        departments.add(parts[12]);
                    }
                }
            }
            totalStudents.setText("Total Students: " + studentCount);
            totalFaculties.setText("Total Faculties: " + faculties.size());
            totalDepartments.setText("Total Departments: " + departments.size());

            // Count total teachers
            int teacherCount = 0;
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("teachers.txt"))) {
                while (br.readLine() != null) {
                    teacherCount++;
                }
            }
            totalTeachers.setText("Total Teachers: " + teacherCount);
        } catch (java.io.FileNotFoundException e) {
            totalStudents.setText("Total Students: Error (file not found)");
            totalTeachers.setText("Total Teachers: Error (file not found)");
            totalFaculties.setText("Total Faculties: Error (file not found)");
            totalDepartments.setText("Total Departments: Error (file not found)");
        } catch (Exception e) {
            totalStudents.setText("Total Students: Error");
            totalTeachers.setText("Total Teachers: Error");
            totalFaculties.setText("Total Faculties: Error");
            totalDepartments.setText("Total Departments: Error");
        }

        root.getChildren().addAll(title, totalStudents, totalTeachers, totalFaculties, totalDepartments);
        Scene scene = new Scene(root, 500, 350);
        stage.setTitle("Statistics");
        stage.setScene(scene);
        stage.show();
    }
}
