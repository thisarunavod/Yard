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
    private AnchorPane mainRoot;

    @FXML
    private AnchorPane root;

    private Parent nodeAddVehicle;

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
    void btnDeleteVehicleOnAction(ActionEvent event) {

    }

    @FXML
    void btnOnActionUpdateVehicle(ActionEvent event) {

    }

    @FXML
    void btnVehicleListOnAction(ActionEvent event) {

    }


}
