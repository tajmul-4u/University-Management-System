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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentAttendanceDetail extends Application {
    private TableView<AttendanceRecord> attendanceTable;
    private ObservableList<AttendanceRecord> attendanceData;
    private Button printButton, refreshButton, exportButton;
    private Label titleLabel, recordCountLabel;
    private final Color PRIMARY_COLOR = Color.web("#2980b9");
    private final Color BACKGROUND_COLOR = Color.web("#ecf0f1");
    private final Color TEXT_COLOR = Color.web("#2c3e50");
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Student Attendance Management System");
        initializeComponents();
        VBox root = setupLayout();
        loadAttendanceData();
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeComponents() {
        // Header
        titleLabel = new Label("Student Attendance Records");
        titleLabel.setFont(Font.font("Segoe UI", 24));
        titleLabel.setTextFill(PRIMARY_COLOR);
        recordCountLabel = new Label("Loading records...");
        recordCountLabel.setFont(Font.font("Segoe UI", 12));
        recordCountLabel.setTextFill(TEXT_COLOR);

        // Table
        attendanceTable = new TableView<>();
        attendanceData = FXCollections.observableArrayList();
        attendanceTable.setItems(attendanceData);
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        attendanceTable.setRowFactory(tv -> new TableRow<AttendanceRecord>() {
            @Override
            protected void updateItem(AttendanceRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getStatus()) {
                        case "Full Day":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                            break;
                        case "Half Day":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;");
                            break;
                        case "Absent":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        setupTableColumns();
        attendanceTable.setPrefHeight(400);

        // Buttons
        printButton = createStyledButton("Print Report");
        refreshButton = createStyledButton("Refresh");
        exportButton = createStyledButton("Export");
        printButton.setOnAction(e -> handlePrintAction());
        refreshButton.setOnAction(e -> handleRefreshAction());
        exportButton.setOnAction(e -> handleExportAction());
    }

    private void setupTableColumns() {
        TableColumn<AttendanceRecord, String> rollCol = new TableColumn<>("Roll Number");
        rollCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        rollCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<AttendanceRecord, String> dateCol = new TableColumn<>("Date & Time");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<AttendanceRecord, String> firstCol = new TableColumn<>("First Half");
        firstCol.setCellValueFactory(new PropertyValueFactory<>("firstHalf"));
        firstCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<AttendanceRecord, String> secondCol = new TableColumn<>("Second Half");
        secondCol.setCellValueFactory(new PropertyValueFactory<>("secondHalf"));
        secondCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<AttendanceRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setStyle("-fx-alignment: CENTER;");
        attendanceTable.getColumns().addAll(rollCol, dateCol, firstCol, secondCol, statusCol);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", 14));
        button.setTextFill(Color.BLUE);
        button.setStyle("-fx-background-color: #2980b9; -fx-background-radius: 8; -fx-padding: 8 16; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #3498db; -fx-background-radius: 8; -fx-padding: 8 16; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #2980b9; -fx-background-radius: 8; -fx-padding: 8 16; -fx-cursor: hand;"));
        button.setPrefWidth(120);
        button.setPrefHeight(35);
        return button;
    }

    private VBox setupLayout() {
        VBox root = new VBox();
        root.setSpacing(0);
        root.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox headerBox = new VBox(5, titleLabel, recordCountLabel);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        HBox buttonBox = new HBox(10, refreshButton, exportButton, printButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 20, 20, 20));
        buttonBox.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox mainBox = new VBox(attendanceTable);
        mainBox.setPadding(new Insets(0, 20, 0, 20));
        mainBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1;");

        root.getChildren().addAll(headerBox, mainBox, buttonBox);
        VBox.setVgrow(mainBox, Priority.ALWAYS);
        return root;
    }

    private void loadAttendanceData() {
        attendanceData.clear();
        new Thread(() -> {
            int recordCount = 0;
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("attendance_student.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String rollNo = parts[0];
                        String date = parts[1];
                        String firstHalf = parts[2];
                        String secondHalf = parts[3];
                        String status = calculateAttendanceStatus(firstHalf, secondHalf);
                        attendanceData.add(new AttendanceRecord(rollNo, date, firstHalf, secondHalf, status));
                        recordCount++;
                    }
                }
                String labelText = recordCount == 0 ? "No attendance records found" :
                        String.format("Total Records: %d | Last Updated: %s", recordCount, java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
                javafx.application.Platform.runLater(() -> recordCountLabel.setText(labelText));
            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    showErrorMessage("File Error", "Error loading attendance data: " + e.getMessage());
                    recordCountLabel.setText("Error loading data");
                });
            }
        }).start();
    }

    private String calculateAttendanceStatus(String firstHalf, String secondHalf) {
        boolean firstPresent = "Present".equalsIgnoreCase(firstHalf);
        boolean secondPresent = "Present".equalsIgnoreCase(secondHalf);
        if (firstPresent && secondPresent) {
            return "Full Day";
        } else if (firstPresent || secondPresent) {
            return "Half Day";
        } else {
            return "Absent";
        }
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handlePrintAction() {
        // JavaFX TableView does not have built-in print, so show info dialog
        showSuccessMessage("Print", "Printing is not implemented in this version. Please export to CSV and print from a spreadsheet application.");
    }

    private void handleRefreshAction() {
        recordCountLabel.setText("Refreshing data...");
        loadAttendanceData();
        showSuccessMessage("Refresh Complete", "Attendance data has been refreshed successfully!");
    }

    private void handleExportAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Attendance Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            // Export functionality can be implemented here
            showSuccessMessage("Export", "Export functionality will be implemented in the next version.");
        }
    }

    public static class AttendanceRecord {
        private final SimpleStringProperty rollNo;
        private final SimpleStringProperty date;
        private final SimpleStringProperty firstHalf;
        private final SimpleStringProperty secondHalf;
        private final SimpleStringProperty status;

        public AttendanceRecord(String rollNo, String date, String firstHalf, String secondHalf, String status) {
            this.rollNo = new SimpleStringProperty(rollNo);
            this.date = new SimpleStringProperty(date);
            this.firstHalf = new SimpleStringProperty(firstHalf);
            this.secondHalf = new SimpleStringProperty(secondHalf);
            this.status = new SimpleStringProperty(status);
        }
        public String getRollNo() { return rollNo.get(); }
        public String getDate() { return date.get(); }
        public String getFirstHalf() { return firstHalf.get(); }
        public String getSecondHalf() { return secondHalf.get(); }
        public String getStatus() { return status.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}