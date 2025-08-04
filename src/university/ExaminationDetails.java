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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;

public class ExaminationDetails extends Application {
    private TableView<StudentRecord> table;
    private ObservableList<StudentRecord> tableData;
    private TextField searchField;
    private Button viewResultButton;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Student Examination Details System");
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.web("#f0f8ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox mainBox = new VBox(30);
        mainBox.setPadding(new Insets(30, 30, 30, 30));
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(createHeader(), createSearchPanel(), createTablePanel());
        root.setCenter(mainBox);
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        loadStudentData();
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        Label title = new Label("Examination Details Portal");
        title.setFont(Font.font("Segoe UI", 36));
        title.setTextFill(Color.web("#191970"));
        title.setEffect(new DropShadow(2, Color.web("#4682b4")));
        headerBox.getChildren().add(title);
        return headerBox;
    }

    private HBox createSearchPanel() {
        HBox searchBox = new HBox(20);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(20, 0, 20, 0));
        searchBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        searchBox.setEffect(new DropShadow(2, Color.web("#4682b4")));
        Label searchLabel = new Label("Search Student Roll Number:");
        searchLabel.setFont(Font.font("Segoe UI", 16));
        searchLabel.setTextFill(Color.web("#191970"));
        searchField = new TextField();
        searchField.setPromptText("Enter student roll number to search");
        searchField.setFont(Font.font("Segoe UI", 16));
        searchField.setPrefWidth(350);
        viewResultButton = new Button("View Result");
        viewResultButton.setFont(Font.font("Segoe UI", 16));
        viewResultButton.setStyle("-fx-background-color: #ff1493; -fx-text-fill: white; -fx-background-radius: 10;");
        viewResultButton.setOnMouseEntered(e -> viewResultButton.setStyle("-fx-background-color: #ff4500; -fx-text-fill: white; -fx-background-radius: 10;"));
        viewResultButton.setOnMouseExited(e -> viewResultButton.setStyle("-fx-background-color: #ff1493; -fx-text-fill: white; -fx-background-radius: 10;"));
        viewResultButton.setOnAction(e -> handleViewResult());
        searchBox.getChildren().addAll(searchLabel, searchField, viewResultButton);
        return searchBox;
    }

    private VBox createTablePanel() {
        VBox tableBox = new VBox();
        tableBox.setAlignment(Pos.CENTER);
        tableBox.setPadding(new Insets(10, 0, 0, 0));
        tableBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        tableBox.setEffect(new DropShadow(2, Color.web("#4682b4")));
        table = new TableView<>();
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        tableBox.getChildren().add(table);
        table.setRowFactory(tv -> {
            TableRow<StudentRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    StudentRecord record = row.getItem();
                    searchField.setText(record.getRollno());
                }
            });
            return row;
        });
        return tableBox;
    }

    private void setupTableColumns() {
        // Example columns, adjust as needed for your DB
        TableColumn<StudentRecord, String> rollCol = new TableColumn<>("Roll No");
        rollCol.setCellValueFactory(new PropertyValueFactory<>("rollno"));
        TableColumn<StudentRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<StudentRecord, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        TableColumn<StudentRecord, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));
        TableColumn<StudentRecord, String> semCol = new TableColumn<>("Semester");
        semCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        table.getColumns().addAll(rollCol, nameCol, courseCol, branchCol, semCol);
    }

    private void loadStudentData() {
        tableData = FXCollections.observableArrayList();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 13) {
                    tableData.add(new StudentRecord(
                        parts[10], // rollno
                        parts[0],  // name
                        parts[11], // course
                        parts[12], // branch
                        ""         // semester (not present in file, leave blank)
                    ));
                }
            }
            table.setItems(tableData);
        } catch (java.io.FileNotFoundException e) {
            showError("Error: students.txt file not found.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading student data: " + e.getMessage());
        }
    }

    private void handleViewResult() {
        String rollNumber = searchField.getText().trim();
        if (rollNumber.isEmpty()) {
            showError("Please enter a roll number to search for results.");
            return;
        }
        try {
            Marks marksWindow = new Marks(rollNumber);
            Stage marksStage = new Stage();
            marksWindow.start(marksStage);
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while processing your request. Please try again.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class StudentRecord {
        private final SimpleStringProperty rollno, name, course, branch, semester;
        public StudentRecord(String rollno, String name, String course, String branch, String semester) {
            this.rollno = new SimpleStringProperty(rollno);
            this.name = new SimpleStringProperty(name);
            this.course = new SimpleStringProperty(course);
            this.branch = new SimpleStringProperty(branch);
            this.semester = new SimpleStringProperty(semester);
        }
        public String getRollno() { return rollno.get(); }
        public String getName() { return name.get(); }
        public String getCourse() { return course.get(); }
        public String getBranch() { return branch.get(); }
        public String getSemester() { return semester.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}