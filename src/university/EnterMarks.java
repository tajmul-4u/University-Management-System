package university;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EnterMarks extends Application {
    private TextField rollField, sub1Field, mark1Field, sub2Field, mark2Field, sub3Field, mark3Field, sub4Field, mark4Field, sub5Field, mark5Field;
    private Button submitButton;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Student Marks Entry System");
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.web("#f0f8ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(30, 30, 30, 30));
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(createHeader(), createFormPanel(), createButtonPanel());
        root.setCenter(mainBox);
        Scene scene = new Scene(root, 600, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        Label title = new Label("Enter Student Marks");
        title.setFont(Font.font("Segoe UI", 28));
        title.setTextFill(Color.web("#191970"));
        title.setEffect(new DropShadow(2, Color.web("#4682b4")));
        headerBox.getChildren().add(title);
        return headerBox;
    }

    private VBox createFormPanel() {
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setPadding(new Insets(20, 0, 20, 0));
        formBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        formBox.setEffect(new DropShadow(2, Color.web("#4682b4")));
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        // Roll Number
        grid.add(new Label("Roll Number:"), 0, 0);
        rollField = createStyledTextField();
        grid.add(rollField, 1, 0);
        // Subjects and Marks
        grid.add(new Label("Subject 1:"), 0, 1);
        sub1Field = createStyledTextField();
        grid.add(sub1Field, 1, 1);
        grid.add(new Label("Marks 1:"), 2, 1);
        mark1Field = createStyledTextField();
        grid.add(mark1Field, 3, 1);
        grid.add(new Label("Subject 2:"), 0, 2);
        sub2Field = createStyledTextField();
        grid.add(sub2Field, 1, 2);
        grid.add(new Label("Marks 2:"), 2, 2);
        mark2Field = createStyledTextField();
        grid.add(mark2Field, 3, 2);
        grid.add(new Label("Subject 3:"), 0, 3);
        sub3Field = createStyledTextField();
        grid.add(sub3Field, 1, 3);
        grid.add(new Label("Marks 3:"), 2, 3);
        mark3Field = createStyledTextField();
        grid.add(mark3Field, 3, 3);
        grid.add(new Label("Subject 4:"), 0, 4);
        sub4Field = createStyledTextField();
        grid.add(sub4Field, 1, 4);
        grid.add(new Label("Marks 4:"), 2, 4);
        mark4Field = createStyledTextField();
        grid.add(mark4Field, 3, 4);
        grid.add(new Label("Subject 5:"), 0, 5);
        sub5Field = createStyledTextField();
        grid.add(sub5Field, 1, 5);
        grid.add(new Label("Marks 5:"), 2, 5);
        mark5Field = createStyledTextField();
        grid.add(mark5Field, 3, 5);
        formBox.getChildren().add(grid);
        return formBox;
    }

    private HBox createButtonPanel() {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        submitButton = new Button("Submit Marks");
        submitButton.setFont(Font.font("Segoe UI", 16));
        submitButton.setStyle("-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #4169e1; -fx-text-fill: white; -fx-background-radius: 10;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"));
        submitButton.setOnAction(e -> handleSubmit());
        buttonBox.getChildren().add(submitButton);
        return buttonBox;
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setFont(Font.font("Segoe UI", 14));
        field.setPrefWidth(120);
        return field;
    }

    private void handleSubmit() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            showError("Roll number cannot be empty.");
            return;
        }

        if (sub1Field.getText().trim().isEmpty() || mark1Field.getText().trim().isEmpty() ||
            sub2Field.getText().trim().isEmpty() || mark2Field.getText().trim().isEmpty() ||
            sub3Field.getText().trim().isEmpty() || mark3Field.getText().trim().isEmpty() ||
            sub4Field.getText().trim().isEmpty() || mark4Field.getText().trim().isEmpty() ||
            sub5Field.getText().trim().isEmpty() || mark5Field.getText().trim().isEmpty()) {
            showError("All subject and mark fields must be filled.");
            return;
        }

        try {
            // Save subjects to file
            String subjectRecord = String.join(",",
                roll,
                sub1Field.getText().trim(),
                sub2Field.getText().trim(),
                sub3Field.getText().trim(),
                sub4Field.getText().trim(),
                sub5Field.getText().trim()
            );
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("subjects.txt", true))) {
                bw.write(subjectRecord);
                bw.newLine();
            }
            // Save marks to file
            String marksRecord = String.join(",",
                roll,
                mark1Field.getText().trim(),
                mark2Field.getText().trim(),
                mark3Field.getText().trim(),
                mark4Field.getText().trim(),
                mark5Field.getText().trim()
            );
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("marks.txt", true))) {
                bw.write(marksRecord);
                bw.newLine();
            }
            showSuccess("Marks Inserted Successfully");
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error inserting data: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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