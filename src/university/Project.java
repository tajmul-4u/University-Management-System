package university;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Project extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Management System - Dashboard");
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        // Gradient background
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#212F3D")), new Stop(1, Color.web("#2C3E50")) };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        BorderPane mainPane = new BorderPane();
        mainPane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Try to add background image as overlay
        try {
            Image bgImg = new Image(getClass().getResourceAsStream("/icons/third.jpg"));
            if (bgImg.isError()) {
                throw new Exception("Background image not found or failed to load.");
            }
            ImageView bgView = new ImageView(bgImg);
            bgView.setPreserveRatio(true);
            StackPane stack = new StackPane(bgView, mainPane);
            Scene scene = new Scene(stack, 1200, 800);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            Scene scene = new Scene(mainPane, 1200, 800);
            primaryStage.setScene(scene);
        }

        // Modern menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #34495E; -fx-padding: 8 10 8 10; -fx-font-size: 14px;");

        // Master Menu
        Menu master = createModernMenu("Join", "#3498DB");
        MenuItem m1 = createModernMenuItem("Join New Teacher", "/icons/icon1.png", "Ctrl+D");
        MenuItem m2 = createModernMenuItem("New Student Admission", "/icons/icon2.png", "Ctrl+M");
        m1.setOnAction(e -> new AddTeacher().start(new Stage()));
        m2.setOnAction(e -> new AddStudent().start(new Stage()));
        master.getItems().addAll(m1, m2);

        // Details Menu
        Menu user = createModernMenu("Details", "#E74C3C");
        MenuItem u1 = createModernMenuItem("Student Details", "/icons/icon3.png", "Ctrl+P");
        MenuItem u2 = createModernMenuItem("Teacher Details", "/icons/icon4.jpg", "Ctrl+B");
        u1.setOnAction(e -> new StudentDetails().start(new Stage()));
        u2.setOnAction(e -> new TeacherDetails().start(new Stage()));
        user.getItems().addAll(u1, u2);

        // Attendance Menu
        Menu attendance = createModernMenu("Attendance", "#2ECC71");
        MenuItem a1 = createModernMenuItem("Student Attendance", "/icons/icon14.jpg", "Ctrl+P");
        MenuItem a2 = createModernMenuItem("Teacher Attendance", "/icons/icon15.png", "Ctrl+B");
        a1.setOnAction(e -> new StudentAttendance().start(new Stage()));
        a2.setOnAction(e -> new TeacherAttendance().start(new Stage()));
        attendance.getItems().addAll(a1, a2);

        // Attendance Detail Menu
        Menu attendanceDetail = createModernMenu("Attendance Detail", "#9B59B6");
        MenuItem b1 = createModernMenuItem("Student Attendance Detail", "/icons/icon15.png", "Ctrl+P");
        MenuItem b2 = createModernMenuItem("Teacher Attendance Detail", "/icons/icon14.jpg", "Ctrl+B");
        b1.setOnAction(e -> new StudentAttendanceDetail().start(new Stage()));
        b2.setOnAction(e -> new TeacherAttendanceDetail().start(new Stage()));
        attendanceDetail.getItems().addAll(b1, b2);

        // Examination Menu
        Menu exam = createModernMenu("Examination", "#E67E22");
        MenuItem c1 = createModernMenuItem("Examination Details", "/icons/icon16.png", "Ctrl+P");
        MenuItem c2 = createModernMenuItem("Enter Marks", "/icons/icon17.png", "Ctrl+B");
        c1.setOnAction(e -> new ExaminationDetails().start(new Stage()));
        c2.setOnAction(e -> new EnterMarks().start(new Stage()));
        exam.getItems().addAll(c1, c2);

        // Update Details Menu
        Menu report = createModernMenu("Update Details", "#3498DB");
        MenuItem r1 = createModernMenuItem("Update Students", "/icons/icon5.png", "Ctrl+R");
        MenuItem r2 = createModernMenuItem("Update Teachers", "/icons/icon6.png", "Ctrl+R");
        r1.setOnAction(e -> new UpdateStudent().start(new Stage()));
        r2.setOnAction(e -> new UpdateTeacher().start(new Stage()));
        report.getItems().addAll(r1, r2);

        // Statistics Menu (separate)
        Menu statisticsMenu = createModernMenu("Statistics", "#8E44AD");
        MenuItem statsItem = createModernMenuItem("View Statistics", "/icons/icon13.jpg", "Ctrl+T");
        statsItem.setOnAction(e -> new Statistics().start(new Stage()));
        statisticsMenu.getItems().add(statsItem);

        // Exit Menu
        Menu exit = createModernMenu("Exit", "#E74C3C");
        MenuItem ex = createModernMenuItem("Exit", "/icons/icon12.png", "Ctrl+Z");
        ex.setOnAction(e -> System.exit(0));
        exit.getItems().add(ex);

        menuBar.getMenus().addAll(master, user, attendance, attendanceDetail, exam, report, statisticsMenu, exit);
        
        HBox menuContainer = new HBox(menuBar);
        menuContainer.setAlignment(Pos.CENTER);
        mainPane.setTop(menuContainer);

        // Welcome panel overlay
        VBox welcomeBox = new VBox(20);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(100, 0, 0, 0));
        Label welcomeLabel = new Label("Welcome to University Management System");
        welcomeLabel.setFont(Font.font("Segoe UI", 48));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-padding: 30 50 30 50; -fx-background-radius: 10;");
        Label subtitleLabel = new Label("Select an option from the menu to get started");
        subtitleLabel.setFont(Font.font("Segoe UI", 24));
        subtitleLabel.setTextFill(Color.WHITE);
        subtitleLabel.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 20 40 20 40; -fx-background-radius: 10;");
        welcomeBox.getChildren().addAll(welcomeLabel, subtitleLabel);
        mainPane.setCenter(welcomeBox);

        // Status bar
        HBox statusBar = new HBox();
        statusBar.setAlignment(Pos.CENTER_RIGHT);
        statusBar.setPadding(new Insets(20));
        Label timeLabel = new Label("System Ready");
        timeLabel.setFont(Font.font("Segoe UI", 14));
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 10 15 10 15; -fx-background-radius: 10;");
        statusBar.getChildren().add(timeLabel);
        mainPane.setBottom(statusBar);

        primaryStage.show();
    }

    private Menu createModernMenu(String text, String colorHex) {
        Menu menu = new Menu(text);
        menu.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");
        return menu;
    }

    private MenuItem createModernMenuItem(String text, String iconPath, String accelerator) {
        MenuItem item = new MenuItem(text);
        item.setStyle("-fx-font-family: 'Segoe UI';");
        if (accelerator != null && !accelerator.isEmpty()) {
            item.setAccelerator(KeyCombination.keyCombination(accelerator));
        }
        try {
            Image icon = new Image(getClass().getResourceAsStream(iconPath), 20, 20, true, true);
            item.setGraphic(new ImageView(icon));
        } catch (Exception e) {
            // If icon not found, continue without icon
        }
        return item;
    }

    }