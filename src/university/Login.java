package university;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Login extends Application {
    private TextField usernameField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Management System - Login");

        // Gradient background
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#4A90E2")), new Stop(1, Color.web("#B4D2FF"))};
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        StackPane background = new StackPane();
        background.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Login card
        VBox loginCard = new VBox(15);
        loginCard.setPadding(new Insets(20));
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        loginCard.setMaxWidth(400);
        loginCard.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10,0,0,2);");

        // Logo
        try {
            Image logo = new Image(getClass().getResourceAsStream("/icons/second.jpg"), 60, 60, true, true);
            ImageView logoView = new ImageView(logo);
            loginCard.getChildren().add(logoView);
        } catch (Exception e) {
            Label placeholder = new Label("\uD83C\uDF93");
            placeholder.setFont(Font.font("Segoe UI Emoji", 30));
            loginCard.getChildren().add(placeholder);
        }

        // Title & subtitle
        Label title = new Label("University Login");
        title.setFont(Font.font("Segoe UI", 28));
        title.setTextFill(Color.web("#2C3E50"));

        Label subtitle = new Label("For Management use only");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#7F8C8D"));

        // Username and Password Fields
        Label userLabel = new Label("Username");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label passLabel = new Label("Password");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        // Buttons
        Button loginBtn = new Button("LOGIN");
        loginBtn.setOnAction(e -> handleLogin(primaryStage));

        Button registerBtn = new Button("REGISTER");
        registerBtn.setOnAction(e -> showRegistrationDialog(primaryStage));

        Button cancelBtn = new Button("CANCEL");
        cancelBtn.setOnAction(e -> primaryStage.close());

        HBox buttonBox = new HBox(20, loginBtn, registerBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Final layout
        loginCard.getChildren().addAll(title, subtitle, userLabel, usernameField, passLabel, passwordField, buttonBox);

        Label footer = new Label("\u00A9 2025 University Management System");
        footer.setFont(Font.font("Segoe UI", 11));
        footer.setTextFill(Color.WHITE);
        footer.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, loginCard, footer);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30, 0, 0, 0));

        background.getChildren().add(root);
        Scene scene = new Scene(background, 650, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showRegistrationDialog(Stage owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("User Registration");
        dialog.initOwner(owner);
        dialog.setHeaderText("Register a new user");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(400);

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Enter username");

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);

        dialogPane.setContent(grid);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> dialogButton);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String username = userField.getText().trim();
                String password = passField.getText().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Username and password cannot be empty.");
                } else {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("login.txt", true))) {
                        bw.write(username + "," + password);
                        bw.newLine();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Registration Error", "Error saving user: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void handleLogin(Stage stage) {
        String u = usernameField.getText().trim();
        String v = passwordField.getText().trim();

        // Dummy checkLogin method (Replace with actual file-based or File check)
        boolean valid = false;
        try (java.util.Scanner scanner = new java.util.Scanner(new java.io.File("login.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 2 && parts[0].equals(u) && parts[1].equals(v)) {
                    valid = true;
                    break;
                }
            }
        } catch (java.io.FileNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Login file not found.");
            return; // Exit the method if the file is not found
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while reading the login file.");
            return; // Exit on other errors as well
        }

        if (valid) {
            showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome, " + u + "!");
            new Project().start(new Stage());
            stage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
