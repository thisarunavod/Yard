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
import lk.ijse.yard.model.SupplierModel;

import java.io.IOException;
import java.sql.SQLException;

public class SuppliersManageFormController {

    private final SupplierModel supplierModel = new SupplierModel();
    private int rawCountSupplier ;

    @FXML
    private JFXButton btnADDmaterials;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private AnchorPane root;

    private Parent nodeAddSupplier;
    private Parent nodeUpdateSupplier;
    private Parent nodeSupplierList;
    private Parent nodeDeleteSupplier;

    @FXML
    void initialize() throws IOException {
        setRawSupCount();
        loadAddSupplierForm();
    }
    private void setRawSupCount(){
        try {
            rawCountSupplier = supplierModel.getRawCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
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

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) throws IOException {  loadAddSupplierForm(); }

    private void loadAddSupplierForm() throws IOException {
        nodeAddSupplier = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/addSupplier_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAddSupplier);
    }

    @FXML
    void btnDeleteSupplierOnAction(ActionEvent event) throws IOException {
        setRawSupCount();
        if (rawCountSupplier > 0){
            nodeUpdateSupplier = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/deleteSupplier_form.fxml"));

            this.root.getChildren().clear();
            this.root.getChildren().add(nodeUpdateSupplier);
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Added Suppliers !!").show();
        }
    }

    @FXML
    void btnOnActionUpdateSupplier(ActionEvent event) throws IOException {
        setRawSupCount();
        if (rawCountSupplier > 0){
            nodeUpdateSupplier = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/updateSupplier_form.fxml"));

            this.root.getChildren().clear();
            this.root.getChildren().add(nodeUpdateSupplier);
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Added Suppliers !!").show();
        }

    }

    @FXML
    void btnOnActionViewSupplierList(ActionEvent event) throws IOException {
        setRawSupCount();
        if (rawCountSupplier > 0){
            nodeUpdateSupplier = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/supplietList _form.fxml"));

            this.root.getChildren().clear();
            this.root.getChildren().add(nodeUpdateSupplier);
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Not Added Suppliers !!").show();
        }
    }


}
