package university;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentAttendance extends Application {
    private ComboBox<String> rollComboBox, firstHalfCombo, secondHalfCombo;
    private Button submitButton, cancelButton;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Student Attendance System");

        // Title
        Label titleLabel = new Label("Student Attendance Management");
        titleLabel.setFont(Font.font("Segoe UI", 24));
        titleLabel.setTextFill(Color.web("#2980B9"));
        titleLabel.setAlignment(Pos.CENTER);

        // Date label
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Label dateLabel = new Label("Today: " + dateFormat.format(new Date()));
        dateLabel.setFont(Font.font("Segoe UI", 12));
        dateLabel.setTextFill(Color.web("#7F8C8D"));
        dateLabel.setAlignment(Pos.CENTER);

        // Form labels and fields
        Label rollLabel = new Label("Select Roll Number:");
        rollLabel.setFont(Font.font("Segoe UI", 14));
        rollComboBox = new ComboBox<>();
        rollComboBox.setPrefWidth(200);
        rollComboBox.setStyle("-fx-font-size: 14;");

        Label firstHalfLabel = new Label("First Half:");
        firstHalfLabel.setFont(Font.font("Segoe UI", 14));
        firstHalfCombo = new ComboBox<>();
        firstHalfCombo.getItems().addAll("Present", "Absent", "Leave");
        firstHalfCombo.setPrefWidth(200);
        firstHalfCombo.setStyle("-fx-font-size: 14;");
        firstHalfCombo.getSelectionModel().selectFirst();

        Label secondHalfLabel = new Label("Second Half:");
        secondHalfLabel.setFont(Font.font("Segoe UI", 14));
        secondHalfCombo = new ComboBox<>();
        secondHalfCombo.getItems().addAll("Present", "Absent", "Leave");
        secondHalfCombo.setPrefWidth(200);
        secondHalfCombo.setStyle("-fx-font-size: 14;");
        secondHalfCombo.getSelectionModel().selectFirst();

        // Buttons
        submitButton = createModernButton("Submit Attendance", "#2ecc71", "#27ae60");
        cancelButton = createModernButton("Cancel", "#e74c3c", "#c0392b");

        // Layout
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(30, 40, 30, 40));
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setStyle("-fx-background-color: white; -fx-border-color: #DCDCDC; -fx-border-width: 1; -fx-background-radius: 8; -fx-border-radius: 8;");
        formGrid.add(rollLabel, 0, 0);
        formGrid.add(rollComboBox, 1, 0);
        formGrid.add(firstHalfLabel, 0, 1);
        formGrid.add(firstHalfCombo, 1, 1);
        formGrid.add(secondHalfLabel, 0, 2);
        formGrid.add(secondHalfCombo, 1, 2);

        HBox buttonBox = new HBox(20, submitButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setStyle("-fx-background-color: white;");

        VBox mainPanel = new VBox(20,
                titleLabel,
                dateLabel,
                formGrid,
                buttonBox
        );
        mainPanel.setPadding(new Insets(20));
        mainPanel.setStyle("-fx-background-color: #F5F5F5;");
        mainPanel.setAlignment(Pos.TOP_CENTER);

        // Event handlers
        submitButton.setOnAction(e -> handleSubmit());
        cancelButton.setOnAction(e -> stage.close());

        Scene scene = new Scene(mainPanel, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        loadStudentData();
    }

    private Button createModernButton(String text, String colorHex, String hoverHex) {
        Button button = new Button(text);
        button.setPrefWidth(180);
        button.setFont(Font.font("Segoe UI", 14));
        button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 8;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverHex + "; -fx-text-fill: white; -fx-background-radius: 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 8;"));
        return button;
    }

    private void loadStudentData() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 11) {
                    rollComboBox.getItems().add(parts[10]); // rollno
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error loading student data: " + e.getMessage());
        }
    }

    private void handleSubmit() {
        if (rollComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please select a roll number.");
            return;
        }
        String firstHalf = firstHalfCombo.getValue();
        String secondHalf = secondHalfCombo.getValue();
        String dateTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
        String rollNumber = rollComboBox.getValue();
        String attendanceRecord = String.join(",",
            rollNumber, dateTime, firstHalf, secondHalf
        );
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("attendance_student.txt", true))) {
            bw.write(attendanceRecord);
            bw.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Attendance recorded successfully!\n\n" +
                            "Roll Number: " + rollNumber + "\n" +
                            "First Half: " + firstHalf + "\n" +
                            "Second Half: " + secondHalf + "\n" +
                            "Date: " + dateTime);
            stage.close();
        } catch (Exception ee) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error recording attendance: " + ee.getMessage());
            ee.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}