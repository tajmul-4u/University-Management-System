package university;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class Marks extends Application {
    private TextArea textArea;
    private String rollNo;

    public Marks() {}
    public Marks(String rollNo) {
        this.rollNo = rollNo;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Marks");
        BorderPane root = new BorderPane();
        VBox topBox = new VBox();
        topBox.setPadding(new Insets(10));
        root.setTop(topBox);

        textArea = new TextArea();
        textArea.setFont(Font.font("Serif", 18));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        if (rollNo != null) {
            mark(rollNo);
        }
    }

    public void mark(String rollNo) {
        try {
            textArea.setText("\tResult of Examination\n\nSubjects:\n");
            boolean subjectFound = false;
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("subjects.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 6 && parts[0].equals(rollNo)) {
                        textArea.appendText("\n\t" + parts[1]);
                        textArea.appendText("\n\t" + parts[2]);
                        textArea.appendText("\n\t" + parts[3]);
                        textArea.appendText("\n\t" + parts[4]);
                        textArea.appendText("\n\t" + parts[5]);
                        textArea.appendText("\n-----------------------------------------\n");
                        subjectFound = true;
                        break;
                    }
                }
            }
            if (!subjectFound) {
                textArea.appendText("\nNo subject record found for Roll No: " + rollNo);
            }
            boolean marksFound = false;
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("marks.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 6 && parts[0].equals(rollNo)) {
                        textArea.appendText("\nMarks:\n");
                        textArea.appendText("\n\t" + parts[1]);
                        textArea.appendText("\n\t" + parts[2]);
                        textArea.appendText("\n\t" + parts[3]);
                        textArea.appendText("\n\t" + parts[4]);
                        textArea.appendText("\n\t" + parts[5]);
                        textArea.appendText("\n-----------------------------------------\n");
                        marksFound = true;
                        break;
                    }
                }
            }
            if (!marksFound) {
                textArea.appendText("\nNo marks record found for Roll No: " + rollNo);
            }
        } catch (java.io.FileNotFoundException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("File Error");
            alert.setHeaderText(null);
            alert.setContentText("Error: File not found. Please make sure subjects.txt and marks.txt exist.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("File Error");
            alert.setHeaderText(null);
            alert.setContentText("Error retrieving data: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        // Example roll number "101" passed to constructor
        Application.launch(Marks.class, "101");
    }

    @Override
    public void init() throws Exception {
        String[] params = getParameters().getRaw().toArray(new String[0]);
        if (params.length > 0) {
            this.rollNo = params[0];
        }
    }
}
