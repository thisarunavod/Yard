package lk.ijse.yard.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardController {
    @FXML
    private AnchorPane root;

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
    void btnOnActionMachine(ActionEvent event) {

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
