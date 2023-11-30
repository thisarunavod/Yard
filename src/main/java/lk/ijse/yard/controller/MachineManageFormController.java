package lk.ijse.yard.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MachineManageFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane mainRoot;

    private Parent nodeAddMachine;
    private Parent nodeUpdateMachine;
    private Parent nodeRemoveMachine;
    private Parent nodeMachineList;
    private Parent nodeMachineIssuedDetails;



    @FXML
    void btnAddMachineOnAction(ActionEvent event) throws IOException {
        nodeAddMachine = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/addMachine_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAddMachine);
    }


    @FXML
    void btnMachineListOnAction(ActionEvent event) throws IOException {
        nodeMachineList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/machineList_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeMachineList);
    }

    @FXML
    void btnUpdateMachineDetailsOnAction(ActionEvent event) throws IOException {
        nodeUpdateMachine = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/updateMachine_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeUpdateMachine);
    }

    @FXML
    void btnRemoveMachineOnAction(ActionEvent event) throws IOException {

        nodeRemoveMachine = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/remooveMachine_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeRemoveMachine);
    }

    @FXML
    void btnMachineIssuedDetailsOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/machineIssueDetails_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Machine Issue Details");
        stage.setScene(scene);
        stage.centerOnScreen();
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


}
