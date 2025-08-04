package university;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;

public class StudentFeeForm extends Application {
    private ComboBox<String> rollComboBox, courseComboBox, branchComboBox, semesterComboBox;
    private TextField nameField, fnameField, feeField;
    private Button payButton, cancelButton;
    private Label statusLabel;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Student Fee Form");
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 40, 30, 40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label heading = new Label("Student Fee Form");
        heading.setFont(Font.font("Segoe UI", 28));
        heading.setTextFill(Color.web("#512da8"));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(18);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setPadding(new Insets(20, 0, 20, 0));

        // Roll Number
        formGrid.add(new Label("Select Roll Number"), 0, 0);
        rollComboBox = new ComboBox<>();
        rollComboBox.setPrefWidth(220);
        formGrid.add(rollComboBox, 1, 0);

        // Name
        formGrid.add(new Label("Name"), 0, 1);
        nameField = new TextField();
        nameField.setPrefWidth(220);
        formGrid.add(nameField, 1, 1);

        // Father's Name
        formGrid.add(new Label("Father's Name"), 0, 2);
        fnameField = new TextField();
        fnameField.setPrefWidth(220);
        formGrid.add(fnameField, 1, 2);

        // Course
        formGrid.add(new Label("Degree"), 0, 3);
        courseComboBox = new ComboBox<>();
        courseComboBox.setPrefWidth(220);
        formGrid.add(courseComboBox, 1, 3);

        // Branch
        formGrid.add(new Label("Department"), 0, 4);
        branchComboBox = new ComboBox<>();
        branchComboBox.setPrefWidth(220);
        formGrid.add(branchComboBox, 1, 4);

        // Semester
        formGrid.add(new Label("Semester"), 0, 5);
        semesterComboBox = new ComboBox<>();
        semesterComboBox.setPrefWidth(220);
        formGrid.add(semesterComboBox, 1, 5);

        // Fee
        formGrid.add(new Label("Total Fee"), 0, 6);
        feeField = new TextField();
        feeField.setPrefWidth(220);
        formGrid.add(feeField, 1, 6);

        // Buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        payButton = new Button("Pay");
        payButton.setFont(Font.font("Segoe UI", 14));
        payButton.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;");
        payButton.setPrefWidth(120);
        payButton.setOnMouseEntered(e -> payButton.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white;"));
        payButton.setOnMouseExited(e -> payButton.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;"));
        payButton.setOnAction(e -> handlePay());
        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Segoe UI", 14));
        cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white;");
        cancelButton.setPrefWidth(120);
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: white;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white;"));
        cancelButton.setOnAction(e -> primaryStage.close());
        buttonBox.getChildren().addAll(payButton, cancelButton);

        statusLabel = new Label("");
        statusLabel.setFont(Font.font("Segoe UI", 13));
        statusLabel.setTextFill(Color.web("#388e3c"));
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        root.getChildren().addAll(heading, formGrid, buttonBox, statusLabel);
        VBox.setVgrow(formGrid, Priority.ALWAYS);

        loadStudentData();
        setupEventListeners();

        Scene scene = new Scene(root, 600, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadStudentData() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
            ObservableList<String> rollList = FXCollections.observableArrayList();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 11) {
                    rollList.add(parts[10]); // rollNo
                }
            }
            rollComboBox.setItems(rollList);
            branchComboBox.setItems(FXCollections.observableArrayList("Computer Science", "Electrical", "Mechanical"));
            semesterComboBox.setItems(FXCollections.observableArrayList("1st Semester", "2nd Semester", "3rd Semester", "4th Semester"));
            courseComboBox.setItems(FXCollections.observableArrayList("BSc", "BBA", "MBA"));
        } catch (java.io.FileNotFoundException e) {
            showError("Failed to load data from file: students.txt not found.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load data from file");
        }
    }

    private void setupEventListeners() {
        rollComboBox.setOnAction(e -> {
            String rollno = rollComboBox.getValue();
            if (rollno == null) return;
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 13 && parts[10].equals(rollno)) {
                        nameField.setText(parts[0]);
                        fnameField.setText(parts[1]);
                        courseComboBox.setValue(parts[11]);
                        branchComboBox.setValue(parts[12]);
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Error fetching student data");
            }
        });
    }

    private void handlePay() {
        String rollno = rollComboBox.getValue();
        String name = nameField.getText();
        String fname = fnameField.getText();
        String course = courseComboBox.getValue();
        String branch = branchComboBox.getValue();
        String semester = semesterComboBox.getValue();
        String fee = feeField.getText();
        if (rollno == null || name.isEmpty() || fname.isEmpty() || course == null || branch == null || semester == null || fee.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("fees.txt", true))) {
            String record = String.join(",", rollno, name, fname, course, branch, semester, fee);
            bw.write(record);
            bw.newLine();
            showSuccess("Fee Paid Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        statusLabel.setText("");
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
