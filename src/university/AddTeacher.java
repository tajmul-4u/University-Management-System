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
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AddTeacher extends Application {
    private TextField nameField, fatherNameField, ageField, dobField, addressField,
            phoneField, emailField, class10Field, class12Field, cnicField, empIdField;
    private ComboBox<String> educationCombo, departmentCombo, designationCombo;
    private Button submitBtn, cancelBtn;
    private Random ran = new Random();
    private long first4 = (ran.nextLong() % 9000L) + 1000L;
    private long first = Math.abs(first4);
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Add New Teacher");

        // Header
        Label titleLabel = new Label("Teacher Registration Form");
        titleLabel.setFont(Font.font("Segoe UI", 28));
        titleLabel.setTextFill(Color.WHITE);
        HBox headerPanel = new HBox(titleLabel);
        headerPanel.setAlignment(Pos.CENTER);
        headerPanel.setPadding(new Insets(20));
        headerPanel.setStyle("-fx-background-color: #9B59B6;");

        // Form
        GridPane formPanel = new GridPane();
        formPanel.setPadding(new Insets(30));
        formPanel.setHgap(20);
        formPanel.setVgap(15);
        formPanel.setStyle("-fx-background-color: white; -fx-border-color: #DCDCDC; -fx-border-width: 1; -fx-background-radius: 8; -fx-border-radius: 8;");

        nameField = createStyledTextField();
        fatherNameField = createStyledTextField();
        ageField = createStyledTextField();
        dobField = createStyledTextField();
        addressField = createStyledTextField();
        phoneField = createStyledTextField();
        emailField = createStyledTextField();
        class10Field = createStyledTextField();
        class12Field = createStyledTextField();
        cnicField = createStyledTextField();
        empIdField = createStyledTextField();
        empIdField.setEditable(true);

        String[] education = {"BSc","Msc", "PhD","BBA", "BE",  "MBA", "MCA", "BA", "BCom",};
        educationCombo = new ComboBox<>();
        educationCombo.getItems().addAll(education);
        educationCombo.setPrefWidth(200);
        educationCombo.setStyle("-fx-font-size: 14;");
        educationCombo.getSelectionModel().selectFirst();

        String[] departments = {"Computer Systems Engineering", "Electronics", "Electrical", "Mechanical", "Civil"};
        departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll(departments);
        departmentCombo.setPrefWidth(200);
        departmentCombo.setStyle("-fx-font-size: 14;");
        departmentCombo.getSelectionModel().selectFirst();


// Inside start(Stage primaryStage)
        String[] designations = {"Professor","Assistant Professor","Associate Professor","Lecturer"};
        designationCombo = new ComboBox<>();
        designationCombo.getItems().addAll(designations);
        designationCombo.setPrefWidth(200);
        designationCombo.setStyle("-fx-font-size: 14;");

        // Row 1
        formPanel.add(createStyledLabel("Name:"), 0, 0);
        formPanel.add(nameField, 1, 0);
        formPanel.add(createStyledLabel("Father's Name:"), 2, 0);
        formPanel.add(fatherNameField, 3, 0);
        // Row 2
        formPanel.add(createStyledLabel("Age:"), 0, 1);
        formPanel.add(ageField, 1, 1);
        formPanel.add(createStyledLabel("Date of Birth:"), 2, 1);
        formPanel.add(dobField, 3, 1);
        // Row 3
        formPanel.add(createStyledLabel("Address:"), 0, 2);
        formPanel.add(addressField, 1, 2);
        formPanel.add(createStyledLabel("Phone:"), 2, 2);
        formPanel.add(phoneField, 3, 2);
        // Row 4
        formPanel.add(createStyledLabel("Email:"), 0, 3);
        formPanel.add(emailField, 1, 3);
        formPanel.add(createStyledLabel("Undergraduate(CGPA):"), 2, 3);
        formPanel.add(class10Field, 3, 3);
        // Row 5
        formPanel.add(createStyledLabel("Graduate(CGPA):"), 0, 4);
        formPanel.add(class12Field, 1, 4);
        formPanel.add(createStyledLabel("NIC No:"), 2, 4);
        formPanel.add(cnicField, 3, 4);
        // Row 6
        formPanel.add(createStyledLabel("Education:"), 0, 5);
        formPanel.add(educationCombo, 1, 5);
        formPanel.add(createStyledLabel("Department:"), 2, 5);
        formPanel.add(departmentCombo, 3, 5);
        // Row 7
        formPanel.add(createStyledLabel("Employee ID:"), 0, 6);
        formPanel.add(empIdField, 1, 6);
        formPanel.add(createStyledLabel("Designation:"), 2, 6);
        formPanel.add(designationCombo, 3, 6);

        // Buttons
        submitBtn = createStyledButton("Submit", "#8E44AD");
        cancelBtn = createStyledButton("Cancel", "#e74c3c");
        HBox buttonPanel = new HBox(20, submitBtn, cancelBtn);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setPadding(new Insets(20, 0, 0, 0));
        buttonPanel.setStyle("-fx-background-color: white;");

        // Main layout
        VBox mainPanel = new VBox(20, headerPanel, formPanel, buttonPanel);
        mainPanel.setPadding(new Insets(20));
        mainPanel.setStyle("-fx-background-color: #F5F5F5;");

        // Event handlers
        submitBtn.setOnAction(e -> handleSubmit());
        cancelBtn.setOnAction(e -> handleCancel());

        Scene scene = new Scene(mainPanel, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        nameField.requestFocus();
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setPrefWidth(200);
        field.setFont(Font.font("Segoe UI", 14));
        return field;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", 14));
        label.setTextFill(Color.web("#34495E"));
        return label;
    }

    private Button createStyledButton(String text, String colorHex) {
        Button button = new Button(text);
        button.setPrefWidth(120);
        button.setFont(Font.font("Segoe UI", 14));
        button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 8;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + darkenColor(colorHex) + "; -fx-text-fill: white; -fx-background-radius: 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 8;"));
        return button;
    }

    private String darkenColor(String colorHex) {
        Color color = Color.web(colorHex);
        color = color.darker();
        return String.format("#%02X%02X%02X", (int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255));
    }

    private void handleSubmit() {
        if (validateFields()) {
            try {
                String name = nameField.getText().trim();
                String fatherName = fatherNameField.getText().trim();
                String age = ageField.getText().trim();
                String dob = dobField.getText().trim();
                String address = addressField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String class10 = class10Field.getText().trim();
                String class12 = class12Field.getText().trim();
                String cnic = cnicField.getText().trim();
                String empId = empIdField.getText().trim();
                String education = educationCombo.getValue();
                String department = departmentCombo.getValue();

                String designation = designationCombo.getValue();
                String record = String.join(",", name, fatherName, age, dob, address, phone, email, class10, class12, cnic, empId, education, department, designation);

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("teachers.txt", true))) {
                    bw.write(record);
                    bw.newLine();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Teacher details have been successfully registered!\nEmployee ID: " + empId);
                alert.showAndWait();
                stage.close();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while saving teacher details: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel? All entered data will be lost.");
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.OK) {
            stage.close();
        }
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty()) {
            showValidationError("Please enter teacher name.");
            nameField.requestFocus();
            return false;
        }
        if (fatherNameField.getText().trim().isEmpty()) {
            showValidationError("Please enter father's name.");
            fatherNameField.requestFocus();
            return false;
        }
        if (ageField.getText().trim().isEmpty()) {
            showValidationError("Please enter age.");
            ageField.requestFocus();
            return false;
        }
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            if (age < 22 || age > 70) {
                showValidationError("Please enter a valid age for a teacher (22-70).");
                ageField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showValidationError("Please enter a valid numeric age.");
            ageField.requestFocus();
            return false;
        }
        if (dobField.getText().trim().isEmpty()) {
            showValidationError("Please enter date of birth.");
            dobField.requestFocus();
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            showValidationError("Please enter phone number.");
            phoneField.requestFocus();
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showValidationError("Please enter email address.");
            emailField.requestFocus();
            return false;
        }
        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            showValidationError("Please enter a valid email address.");
            emailField.requestFocus();
            return false;
        }
        if (cnicField.getText().trim().isEmpty()) {
            showValidationError("Please enter NIC number.");
            cnicField.requestFocus();
            return false;
        }
        if (empIdField.getText().trim().isEmpty()) {
            showValidationError("Please enter employee ID.");
            empIdField.requestFocus();
            return false;
        }
        return true;
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}