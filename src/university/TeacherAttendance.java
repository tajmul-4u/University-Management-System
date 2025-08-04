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

public class TeacherAttendance extends Application {
    private ComboBox<String> teacherIdComboBox, firstHalfCombo, secondHalfCombo;
    private Button submitButton, cancelButton;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Teacher Attendance System");

        // Title
        Label titleLabel = new Label("Teacher Attendance Management");
        titleLabel.setFont(Font.font("Segoe UI", 24));
        titleLabel.setTextFill(Color.web("#9B59B6"));
        titleLabel.setAlignment(Pos.CENTER);

        // Date label
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Label dateLabel = new Label("Today: " + dateFormat.format(new Date()));
        dateLabel.setFont(Font.font("Segoe UI", 12));
        dateLabel.setTextFill(Color.web("#7F8C8D"));
        dateLabel.setAlignment(Pos.CENTER);

        // Form labels and fields
        Label teacherIdLabel = new Label("Select Teacher ID:");
        teacherIdLabel.setFont(Font.font("Segoe UI", 14));
        teacherIdComboBox = new ComboBox<>();
        teacherIdComboBox.setPrefWidth(200);
        teacherIdComboBox.setStyle("-fx-font-size: 14;");

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
        submitButton = createModernButton("Submit Attendance", "#8E44AD", "#7D3C98");
        cancelButton = createModernButton("Cancel", "#e74c3c", "#c0392b");

        // Layout
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(30, 40, 30, 40));
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setStyle("-fx-background-color: white; -fx-border-color: #DCDCDC; -fx-border-width: 1; -fx-background-radius: 8; -fx-border-radius: 8;");
        formGrid.add(teacherIdLabel, 0, 0);
        formGrid.add(teacherIdComboBox, 1, 0);
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
        mainPanel.setStyle("-fx-background-color: #F4ECF7;");
        mainPanel.setAlignment(Pos.TOP_CENTER);

        // Event handlers
        submitButton.setOnAction(e -> handleSubmit());
        cancelButton.setOnAction(e -> stage.close());

        Scene scene = new Scene(mainPanel, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        loadTeacherData();
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

    private void loadTeacherData() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("teachers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    teacherIdComboBox.getItems().add(parts[0]); // emp_id
                }
            }
        } catch (java.io.FileNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error: teachers.txt file not found.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error loading teacher data: " + e.getMessage());
        }
    }

    private void handleSubmit() {
        if (teacherIdComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please select a teacher ID.");
            return;
        }
        String firstHalf = firstHalfCombo.getValue();
        String secondHalf = secondHalfCombo.getValue();
        String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        String teacherId = teacherIdComboBox.getValue();
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("attendance_teacher.txt", true))) {
            String record = String.join(",", teacherId, dateTime, firstHalf, secondHalf);
            bw.write(record);
            bw.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Teacher attendance recorded successfully!\n\n" +
                            "Teacher ID: " + teacherId + "\n" +
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