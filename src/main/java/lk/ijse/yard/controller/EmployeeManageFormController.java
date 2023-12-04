package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.model.EmployeeModel;

import java.io.IOException;
import java.sql.SQLException;

public class EmployeeManageFormController {

    @FXML
    private JFXButton btnADDEmployee;

    @FXML
    private JFXButton btnADDmaterials111;

    @FXML
    private JFXButton btnListOfEmployee;

    @FXML
    private JFXButton btnUpdateEmployee;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane mainRoot;

    private int EmployeRawCount;
    public Parent nodeAddEmployee;
    private Parent nodeUpdateEmployee;
    private Parent nodeEmployeeList;
    private Parent nodeDeleteEmployee;
    private Parent nodeLaborList;
    private Parent nodeAllEmployeeList;

    @FXML
    void initialize() throws IOException {
        setEmployeRawCount();

        nodeAllEmployeeList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/AllEmployeeList_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAllEmployeeList);
    }

    @FXML
    void btnDriverListOnAction(ActionEvent event) throws IOException {
        setEmployeRawCount();
        if (EmployeRawCount > 0){
            nodeEmployeeList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/driverList_form.fxml"));
            this.root.getChildren().clear();
            this.root.getChildren().add(nodeEmployeeList);

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Added Employees !! ").show();
        }

    }

    @FXML
    void btnLaborListOnAction(ActionEvent event) throws IOException {
        nodeLaborList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/LaborList_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeLaborList);

    }

    @FXML
    void btnAddEmployeeOnAction(ActionEvent event) throws IOException {

        if (nodeAddEmployee == null){
            nodeAddEmployee = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/addEmployee_form.fxml"));
        }
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAddEmployee);

    }

    @FXML
    void btnDeleteEmployeeOnAction(ActionEvent event) throws IOException {
        setEmployeRawCount();
        if (EmployeRawCount > 0){
            nodeDeleteEmployee = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/deleteEmployee_form.fxml"));
            this.root.getChildren().clear();
            this.root.getChildren().add(nodeDeleteEmployee);

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Added Employees !! ").show();
        }
    }


    @FXML
    void btnOnActionListOfEmployee(ActionEvent event) throws IOException {
        nodeAllEmployeeList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/AllEmployeeList_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAllEmployeeList);
    }

    @FXML
    void btnOnActionUpdateEmployee(ActionEvent event) throws IOException {
        setEmployeRawCount();
        if (EmployeRawCount > 0){

            nodeUpdateEmployee = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/employees/updateEmployee_form.fxml"));
            this.root.getChildren().clear();
            this.root.getChildren().add(nodeUpdateEmployee);

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Added Employees !! ").show();
        }

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


    private void setEmployeRawCount(){
        try {
            EmployeRawCount = EmployeeModel.getRawCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }





}
