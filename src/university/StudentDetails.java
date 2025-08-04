package university;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;

public class StudentDetails extends Application {
    private TableView<Student> table;
    private ObservableList<Student> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Details");
        table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14;");

        String[] columnNames = {
                "Name", "Father's Name", "Age", "DOB", "Address", "Phone", "Email",
                "Class X (%)", "Class XII (%)", "NIC No", "Roll No", "Degree", "Department"
        };

        String[] propertyNames = {
                "name", "fathersName", "age", "dob", "address", "phone", "email",
                "classX", "classXII", "nic", "rollNo", "Degree", "Department"
        };

        for (int i = 0; i < columnNames.length; i++) {
            TableColumn<Student, String> col = new TableColumn<>(columnNames[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            col.setStyle("-fx-alignment: CENTER;");
            if (i == 4 || i == 6) { // Address, Email
                col.setCellFactory(tc -> {
                    TableCell<Student, String> cell = new TableCell<>() {
                        private final TextArea textArea = new TextArea();
                        {
                            textArea.setWrapText(true);
                            textArea.setEditable(false);
                            textArea.setFont(Font.font("Segoe UI", 14));
                            setGraphic(textArea);
                        }
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                textArea.setText("");
                            } else {
                                textArea.setText(item);
                            }
                        }
                    };
                    cell.setAlignment(Pos.CENTER);
                    return cell;
                });
            }
            table.getColumns().add(col);
        }

        table.setItems(data);
        table.setFixedCellSize(60);

        BorderPane root = new BorderPane(table);
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadData();
    }

    private void loadData() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 13) {
                    data.add(new Student(
                        parts[0], // name
                        parts[1], // fathersName
                        parts[2], // age
                        parts[3], // dob
                        parts[4], // address
                        parts[5], // phone
                        parts[6], // email
                        parts[7], // classX
                        parts[8], // classXII
                        parts[9], // cnic
                        parts[10], // rollNo
                        parts[11], // course
                        parts[12]  // branch
                    ));
                }
            }
        } catch (java.io.FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error: students.txt file not found.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading data: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static class Student {
        private final SimpleStringProperty name, fathersName, age, dob, address, phone, email, classX, classXII, cnic, rollNo, course, branch;
        public Student(String name, String fathersName, String age, String dob, String address, String phone, String email, String classX, String classXII, String cnic, String rollNo, String course, String branch) {
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
            this.rollNo = new SimpleStringProperty(rollNo);
            this.course = new SimpleStringProperty(course);
            this.branch = new SimpleStringProperty(branch);
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
        public String getRollNo() { return rollNo.get(); }
        public String getCourse() { return course.get(); }
        public String getBranch() { return branch.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
