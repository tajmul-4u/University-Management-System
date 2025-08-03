package university;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class TeacherDetails extends Application {
    private TableView<Teacher> table;
    private ObservableList<Teacher> data = FXCollections.observableArrayList();
    private TextField empIdField;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("University Management System - Teacher Details");

        // Header
        Label titleLabel = new Label("Teacher Management Dashboard");
        titleLabel.setFont(Font.font("Segoe UI", 28));
        titleLabel.setTextFill(Color.WHITE);
        HBox headerPanel = new HBox(titleLabel);
        headerPanel.setAlignment(Pos.CENTER);
        headerPanel.setPadding(new Insets(15, 20, 15, 20));
        headerPanel.setStyle("-fx-background-color: #3F51B5;");

        // Table
        table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14;");
        String[] columnNames = {"Name","Father's Name","Age","Date of Birth","Address","Phone","Email","Class X(%)", "Class XII(%)", "CNIC No","Course","Department", "Employee Id"};
        String[] propertyNames = {"name","fathersName","age","dob","address","phone","email","classX","classXII","cnic","course","department","empId"};
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn<Teacher, String> col = new TableColumn<>(columnNames[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            col.setStyle("-fx-alignment: CENTER;");
            table.getColumns().add(col);
        }
        table.setItems(data);
        table.setFixedCellSize(30);
        VBox tablePanel = new VBox(table);
        tablePanel.setPadding(new Insets(10, 20, 10, 20));
        tablePanel.setStyle("-fx-background-color: #F5F5F5;");

        // Action Panel
        HBox actionPanel = new HBox(20);
        actionPanel.setPadding(new Insets(20, 20, 25, 20));
        actionPanel.setAlignment(Pos.CENTER);
        actionPanel.setStyle("-fx-background-color: #F5F5F5;");

        // Delete Panel
        VBox deletePanel = new VBox(10);
        deletePanel.setPadding(new Insets(10));
        deletePanel.setStyle("-fx-background-color: white; -fx-border-color: #BDBDBD; -fx-border-radius: 8; -fx-background-radius: 8;");
        Label deleteLabel = new Label("Delete Teacher by Employee ID:");
        deleteLabel.setFont(Font.font("Segoe UI", 12));
        empIdField = new TextField();
        empIdField.setPromptText("Employee ID");
        empIdField.setPrefWidth(150);
        Button deleteBtn = createStyledButton("Delete", "#F44336");
        deleteBtn.setOnAction(e -> handleDelete());
        deletePanel.getChildren().addAll(deleteLabel, empIdField, deleteBtn);
        deletePanel.setAlignment(Pos.CENTER);

        // Add Panel
        VBox addPanel = new VBox(10);
        addPanel.setPadding(new Insets(10));
        addPanel.setStyle("-fx-background-color: white; -fx-border-color: #BDBDBD; -fx-border-radius: 8; -fx-background-radius: 8;");
        Label addLabel = new Label("Add New Teacher");
        addLabel.setFont(Font.font("Segoe UI", 12));
        Button addBtn = createStyledButton("Add Teacher", "#4CAF50");
        addBtn.setOnAction(e -> new AddTeacher().start(new Stage()));
        addPanel.getChildren().addAll(addLabel, addBtn);
        addPanel.setAlignment(Pos.CENTER);

        // Update Panel
        VBox updatePanel = new VBox(10);
        updatePanel.setPadding(new Insets(10));
        updatePanel.setStyle("-fx-background-color: white; -fx-border-color: #BDBDBD; -fx-border-radius: 8; -fx-background-radius: 8;");
        Label updateLabel = new Label("Update Teacher Details");
        updateLabel.setFont(Font.font("Segoe UI", 12));
        Button updateBtn = createStyledButton("Update", "#FF5722");
        updateBtn.setOnAction(e -> new UpdateTeacher().start(new Stage()));
        updatePanel.getChildren().addAll(updateLabel, updateBtn);
        updatePanel.setAlignment(Pos.CENTER);

        actionPanel.getChildren().addAll(deletePanel, addPanel, updatePanel);

        BorderPane root = new BorderPane();
        root.setTop(headerPanel);
        root.setCenter(tablePanel);
        root.setBottom(actionPanel);
        root.setStyle("-fx-background-color: #F5F5F5;");

        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadData();
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

    private void handleDelete() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter an Employee ID to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete teacher with Employee ID: " + empId + "?");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.OK) {
            try {
                java.io.File inputFile = new java.io.File("teachers.txt");
                java.io.File tempFile = new java.io.File("teachers_temp.txt");
                boolean found = false;
                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(inputFile));
                     java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(tempFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length > 0 && parts[0].equals(empId)) {
                            found = true;
                            continue; // skip this line
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                }
                if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                    showAlert(Alert.AlertType.ERROR, "File Error", "Could not update teacher file.");
                    return;
                }
                if (found) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Teacher deleted successfully!");
                    stage.close();
                    new TeacherDetails().start(new Stage());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "No teacher found with Employee ID: " + empId);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Error deleting teacher: " + e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadData() {
        data.clear();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("teachers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 13) {
                    data.add(new Teacher(
                        parts[0], // name (emp_id)
                        parts[1], // fathersName
                        parts[2], // age
                        parts[3], // dob
                        parts[4], // address
                        parts[5], // phone
                        parts[6], // email
                        parts[7], // classX
                        parts[8], // classXII
                        parts[9], // cnic
                        parts[10], // course
                        parts[11], // department
                        parts[12]  // empId
                    ));
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading data: " + e.getMessage());
        }
    }

    public static class Teacher {
        private final SimpleStringProperty name, fathersName, age, dob, address, phone, email, classX, classXII, cnic, course, department, empId;
        public Teacher(String name, String fathersName, String age, String dob, String address, String phone, String email, String classX, String classXII, String cnic, String course, String department, String empId) {
            this.name = new SimpleStringProperty(name);
            this.fathersName = new SimpleStringProperty(fathersName);
            this.age = new SimpleStringProperty(age);
            this.dob = new SimpleStringProperty(dob);
            this.address = new SimpleStringProperty(address);
            this.phone = new SimpleStringProperty(phone);
            this.email = new SimpleStringProperty(email);
            this.classX = new SimpleStringProperty(classX);
            this.classXII = new SimpleStringProperty(classXII);
            this.cnic = new SimpleStringProperty(cnic);
            this.course = new SimpleStringProperty(course);
            this.department = new SimpleStringProperty(department);
            this.empId = new SimpleStringProperty(empId);
        }
        public String getName() { return name.get(); }
        public String getFathersName() { return fathersName.get(); }
        public String getAge() { return age.get(); }
        public String getDob() { return dob.get(); }
        public String getAddress() { return address.get(); }
        public String getPhone() { return phone.get(); }
        public String getEmail() { return email.get(); }
        public String getClassX() { return classX.get(); }
        public String getClassXII() { return classXII.get(); }
        public String getCnic() { return cnic.get(); }
        public String getCourse() { return course.get(); }
        public String getDepartment() { return department.get(); }
        public String getEmpId() { return empId.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}