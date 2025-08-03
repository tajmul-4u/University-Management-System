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

public class UpdateTeacher extends Application {
    private TextField nameField, fnameField, ageField, dobField, addressField, phoneField, emailField, classXField, classXiiField, cnicField, empIdField, educationField, deptField, searchField;
    private Button updateButton, cancelButton, searchButton;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Update Teacher Details");
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.web("#fff8f0"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(30, 30, 30, 30));
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(createHeader(), createSearchPanel(), createFormPanel(), createButtonPanel());
        root.setCenter(mainBox);
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        Label title = new Label("Update Teacher Details");
        title.setFont(Font.font("Arial", 28));
        title.setTextFill(Color.web("#8b4513"));
        headerBox.getChildren().add(title);
        return headerBox;
    }

    private HBox createSearchPanel() {
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        searchBox.setBackground(new Background(new BackgroundFill(Color.web("#ffe4e1"), new CornerRadii(8), Insets.EMPTY)));
        Label searchLabel = new Label("Enter Employee ID:");
        searchLabel.setFont(Font.font("Arial", 14));
        searchField = new TextField();
        searchField.setFont(Font.font("Arial", 14));
        searchField.setPrefWidth(180);
        searchButton = new Button("Search");
        searchButton.setFont(Font.font("Arial", 12));
        searchButton.setStyle("-fx-background-color: #cd5c5c; -fx-text-fill: white;");
        searchButton.setOnAction(e -> handleSearch());
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);
        return searchBox;
    }

    private GridPane createFormPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(18);
        grid.setPadding(new Insets(20, 0, 20, 0));
        grid.setAlignment(Pos.CENTER);
        // Row 1
        nameField = createStyledTextField();
        fnameField = createStyledTextField();
        grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Father's Name:"), 2, 0); grid.add(fnameField, 3, 0);
        // Row 2
        ageField = createStyledTextField();
        dobField = createStyledTextField();
        grid.add(new Label("Age:"), 0, 1); grid.add(ageField, 1, 1);
        grid.add(new Label("DOB (dd/mm/yyyy):"), 2, 1); grid.add(dobField, 3, 1);
        // Row 3
        addressField = createStyledTextField();
        phoneField = createStyledTextField();
        grid.add(new Label("Address:"), 0, 2); grid.add(addressField, 1, 2);
        grid.add(new Label("Phone:"), 2, 2); grid.add(phoneField, 3, 2);
        // Row 4
        emailField = createStyledTextField();
        classXField = createStyledTextField();
        grid.add(new Label("Email ID:"), 0, 3); grid.add(emailField, 1, 3);
        grid.add(new Label("Class X (%):"), 2, 3); grid.add(classXField, 3, 3);
        // Row 5
        classXiiField = createStyledTextField();
        cnicField = createStyledTextField();
        grid.add(new Label("Class XII (%):"), 0, 4); grid.add(classXiiField, 1, 4);
        grid.add(new Label("CNIC:"), 2, 4); grid.add(cnicField, 3, 4);
        // Row 6
        empIdField = createStyledTextField();
        educationField = createStyledTextField();
        grid.add(new Label("Employee ID:"), 0, 5); grid.add(empIdField, 1, 5);
        grid.add(new Label("Education:"), 2, 5); grid.add(educationField, 3, 5);
        // Row 7
        deptField = createStyledTextField();
        grid.add(new Label("Department:"), 0, 6); grid.add(deptField, 1, 6);
        return grid;
    }

    private HBox createButtonPanel() {
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        updateButton = new Button("Update Teacher");
        updateButton.setFont(Font.font("Arial", 14));
        updateButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white;");
        updateButton.setPrefWidth(150);
        updateButton.setOnAction(e -> handleUpdate());
        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", 14));
        cancelButton.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white;");
        cancelButton.setPrefWidth(150);
        cancelButton.setOnAction(e -> primaryStage.close());
        buttonBox.getChildren().addAll(updateButton, cancelButton);
        return buttonBox;
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setFont(Font.font("Arial", 12));
        field.setPrefWidth(180);
        return field;
    }

    private void handleUpdate() {
        try {
            String empId = empIdField.getText().trim();
            if (empId.isEmpty()) {
                showError("Please enter an Employee ID to update.");
                return;
            }
            java.io.File inputFile = new java.io.File("teachers.txt");
            java.io.File tempFile = new java.io.File("teachers_temp.txt");
            boolean updated = false;
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(inputFile));
                 java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 13 && parts[0].equals(empId)) {
                        String updatedRecord = String.join(",",
                            empIdField.getText(),
                            fnameField.getText(),
                            ageField.getText(),
                            dobField.getText(),
                            addressField.getText(),
                            phoneField.getText(),
                            emailField.getText(),
                            classXField.getText(),
                            classXiiField.getText(),
                            cnicField.getText(),
                            educationField.getText(),
                            deptField.getText(),
                            empIdField.getText()
                        );
                        writer.write(updatedRecord);
                        writer.newLine();
                        updated = true;
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                showError("Could not update teacher file.");
                return;
            }
            if (updated) {
                showSuccess("Successfully Updated!");
                primaryStage.close();
            } else {
                showError("No teacher found with Employee ID: " + empId);
            }
        } catch (Exception e) {
            showError("The error is: " + e.getMessage());
        }
    }

    private void handleSearch() {
        String empId = searchField.getText().trim();
        if (empId.isEmpty()) {
            showError("Please enter an Employee ID to search.");
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("teachers.txt"))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 13 && parts[0].equals(empId)) {
                    nameField.setText(parts[0]);
                    fnameField.setText(parts[1]);
                    ageField.setText(parts[2]);
                    dobField.setText(parts[3]);
                    addressField.setText(parts[4]);
                    phoneField.setText(parts[5]);
                    emailField.setText(parts[6]);
                    classXField.setText(parts[7]);
                    classXiiField.setText(parts[8]);
                    cnicField.setText(parts[9]);
                    empIdField.setText(parts[0]);
                    educationField.setText(parts[10]);
                    deptField.setText(parts[11]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                showError("Teacher not found!");
            }
        } catch (Exception ex) {
            showError("Error occurred while searching!");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}