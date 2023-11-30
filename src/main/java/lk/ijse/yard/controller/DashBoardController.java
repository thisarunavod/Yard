package lk.ijse.yard.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DashBoardController {
    @FXML
    private  Label label;
    final DateFormat format = DateFormat.getInstance();


    @FXML
    private AnchorPane root;

    @FXML
    public void initialize(){
        updateTime(label);

        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(1),
                        event -> updateTime(label)
                )
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();

    }

    private void updateTime(Label timeLabel) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        timeLabel.setText(formattedTime);
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/Login_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Login");

    }

    @FXML
    void btnOnActionmanageMaterials(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/MaterialManage_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Manage Materials");


    }

    @FXML
    void btnOnActionManageSuppliers(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/SuppliersManage_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Manage Suppliers");


    }

    @FXML
    void btnOnActionProjects(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/projects_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();
        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Projects");

    }

    @FXML
    void btnOnActionEmployee(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employeeManage_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Manage Materials");



    }

    @FXML
    void btnOnActionMachine(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/machineManage_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Manage Machines");

    }

    @FXML
    void btnOnActionVehicles(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/vehicleManage_form.fxml"));
        Scene scene = new Scene(rootNode);

        root.getChildren().clear();

        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Manage Vehicles");

    }

    @FXML
    void btnOnActionDWR(ActionEvent event) {

    }

}
