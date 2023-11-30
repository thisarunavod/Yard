package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class VehicleManageFormController {

    @FXML
    private JFXButton btnADDEmployee;

    @FXML
    private JFXButton btnUpdateEmployee;

    @FXML
    private JFXButton btnDailyAvailableVehicleList;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private AnchorPane root;

    private Parent nodeAddVehicle;
    private Parent nodeUpdateVehicle;
    private Parent nodeRemoveVehicle;
    private Parent nodeListOfVehicle;
    private Parent nodeDailyAvailableVehicleList;

    @FXML
    void initialize() throws IOException {
        nodeListOfVehicle = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/vehicleList_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeListOfVehicle);
    }

    @FXML
    void btnAddVehicleOnAction(ActionEvent event) throws IOException {

        nodeAddVehicle = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/addVehicle_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAddVehicle);

    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/dash_Board.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Dash board");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnDeleteVehicleOnAction(ActionEvent event) throws IOException {

        nodeRemoveVehicle = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/remove_vehicle_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeRemoveVehicle);

    }

    @FXML
    void btnOnActionUpdateVehicle(ActionEvent event) throws IOException {
        nodeUpdateVehicle = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/updateVehicle_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeUpdateVehicle);

    }

    @FXML
    void btnVehicleListOnAction(ActionEvent event) throws IOException {
        nodeListOfVehicle = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/vehicleList_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeListOfVehicle);
    }

    @FXML
    void btnOnActionDailyAvailableVehicleList(ActionEvent event) throws IOException {
        nodeDailyAvailableVehicleList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/dailyAvailableVehicleList_form.fxml"));
        Scene scene = new Scene(nodeDailyAvailableVehicleList);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Daily Available Vehicles");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnOnActionVehicleRepairDetails(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/vehicleRepair_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Vehicle Repair Details");
        stage.setScene(scene);
        stage.centerOnScreen();
    }


}
