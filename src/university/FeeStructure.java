package university;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FeeStructure extends Application {
    private TableView<FeeRow> feeTable;
    private ObservableList<FeeRow> tableData;
    private final Color PRIMARY_COLOR = Color.web("#2962ff");
    private final Color ACCENT_COLOR = Color.web("#10b981");
    private final Color TEXT_LIGHT = Color.web("#6b7280");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Fee Structure - Academic Portal");
        VBox root = new VBox(30);
        root.setPadding(new Insets(40));
        root.setBackground(new Background(new BackgroundFill(Color.web("#f8fafc"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(createHeaderPane(), createTablePane());
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeaderPane() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 10, 0));
        headerBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), Insets.EMPTY)));
        headerBox.setBorder(new Border(new BorderStroke(PRIMARY_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));

        Label iconLabel = new Label("\uD83C\uDF93");
        iconLabel.setFont(Font.font("Segoe UI Emoji", 64));
        iconLabel.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("University Fee Structure");
        titleLabel.setFont(Font.font("Segoe UI", 52));
        titleLabel.setTextFill(PRIMARY_COLOR);
        titleLabel.setAlignment(Pos.CENTER);

        Label subtitleLabel = new Label("Academic Year 2025-2026 • Fee Schedule");
        subtitleLabel.setFont(Font.font("Segoe UI", 18));
        subtitleLabel.setTextFill(TEXT_LIGHT);
        subtitleLabel.setAlignment(Pos.CENTER);

        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER);
        Label statusDot = new Label("●");
        statusDot.setFont(Font.font("Segoe UI", 12));
        statusDot.setTextFill(ACCENT_COLOR);
        Label statusText = new Label("Fee Structure Active");
        statusText.setFont(Font.font("Segoe UI", 14));
        statusText.setTextFill(TEXT_LIGHT);
        statusBox.getChildren().addAll(statusDot, statusText);

        headerBox.getChildren().addAll(iconLabel, titleLabel, subtitleLabel, statusBox);
        return headerBox;
    }

    private VBox createTablePane() {
        VBox tableBox = new VBox();
        tableBox.setAlignment(Pos.CENTER);
        tableBox.setPadding(new Insets(30, 0, 0, 0));
        tableBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        tableBox.setBorder(new Border(new BorderStroke(Color.web("#e5e7eb"), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));

        feeTable = new TableView<>();
        feeTable.setPrefHeight(500);
        feeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        populateTable();
        tableBox.getChildren().add(feeTable);
        return tableBox;
    }

    private void setupTableColumns() {
        String[] columnNames = {"Semester", "BTech", "BCA", "BBA", "BSc", "MBA", "MCA", "MTech"};
        String[] propertyNames = {"semester", "btech", "bca", "bba", "bsc", "mba", "mca", "mtech"};
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn<FeeRow, String> col = new TableColumn<>(columnNames[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            col.setStyle("-fx-alignment: CENTER;");
            feeTable.getColumns().add(col);
        }
    }

    private void populateTable() {
        tableData = FXCollections.observableArrayList();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("fees.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    tableData.add(new FeeRow(
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]
                    ));
                }
            }
            feeTable.setItems(tableData);
        } catch (Exception e) {
            // If file not found or error, fallback to hardcoded values
            tableData.addAll(
                new FeeRow("Semester 1", "40,000", "35,000", "30,000", "28,000", "50,000", "45,000", "48,000"),
                new FeeRow("Semester 2", "42,000", "36,000", "31,000", "29,000", "52,000", "46,000", "49,000"),
                new FeeRow("Semester 3", "44,000", "37,000", "32,000", "30,000", "54,000", "47,000", "50,000")
            );
            feeTable.setItems(tableData);
        }
    }

    public static class FeeRow {
        private final SimpleStringProperty semester, btech, bca, bba, bsc, mba, mca, mtech;
        public FeeRow(String semester, String btech, String bca, String bba, String bsc, String mba, String mca, String mtech) {
            this.semester = new SimpleStringProperty(semester);
            this.btech = new SimpleStringProperty(btech);
            this.bca = new SimpleStringProperty(bca);
            this.bba = new SimpleStringProperty(bba);
            this.bsc = new SimpleStringProperty(bsc);
            this.mba = new SimpleStringProperty(mba);
            this.mca = new SimpleStringProperty(mca);
            this.mtech = new SimpleStringProperty(mtech);
        }
        public String getSemester() { return semester.get(); }
        public String getBtech() { return btech.get(); }
        public String getBca() { return bca.get(); }
        public String getBba() { return bba.get(); }
        public String getBsc() { return bsc.get(); }
        public String getMba() { return mba.get(); }
        public String getMca() { return mca.get(); }
        public String getMtech() { return mtech.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
