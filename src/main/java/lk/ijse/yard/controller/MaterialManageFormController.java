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
import lk.ijse.yard.model.MaterialModel;

import java.io.IOException;
import java.sql.SQLException;

public class MaterialManageFormController {

    private final MaterialModel materialModel = new MaterialModel();
    private int rawCountItem;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    public  AnchorPane root;

    @FXML
    private JFXButton btnADDmaterials;

    public  Parent  nodeAddmaterial;
    private Parent nodeUpdateMaterial;
    private Parent nodeMaterialList;
    private Parent nodeDeleteMaterial;



    @FXML
    void initialize() throws IOException {
        setRawMaterialCount();
        loadAddMaterialsForm();
    }

    private void setRawMaterialCount(){
        try {
            rawCountItem = materialModel.getRawCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnAddMaterialsOnAction(ActionEvent event) throws IOException {

        if (nodeAddmaterial == null){
            nodeAddmaterial = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/addMaterial_form.fxml"));
        }
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAddmaterial);

    }

    @FXML
    void btnOnActionUpdateMaterials(ActionEvent event) throws IOException {
        setRawMaterialCount();
        if (rawCountItem > 0){
            nodeUpdateMaterial = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/updateMaterial_form.fxml"));
            this.root.getChildren().clear();
            this.root.getChildren().add(nodeUpdateMaterial);
        } else {
            new Alert(Alert.AlertType.INFORMATION,"Not Added Items !!").show();
        }
    }

    @FXML
    void btnOnActionMaterialList(ActionEvent event) throws IOException {
        setRawMaterialCount();
        if (rawCountItem > 0){
            nodeMaterialList = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/materialList_form.fxml"));
            this.root.getChildren().clear();
            this.root.getChildren().add(nodeMaterialList);
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not Added Items !!").show();
        }
    }

    @FXML
    void btnDeleteMaterialOnAction(ActionEvent event) throws IOException {
        setRawMaterialCount();
        if (rawCountItem > 0){
            nodeDeleteMaterial = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/deleteMaterial_form.fxml"));
            this.root.getChildren().clear();
            this.root.getChildren().add(nodeDeleteMaterial);
        } else {
            new Alert(Alert.AlertType.INFORMATION,"Not Added Items !!").show();
        }
    }

    @FXML
    void btnReceivedDetailsOnAction(ActionEvent event) throws IOException {
        setRawMaterialCount();
        if (rawCountItem > 0){
            Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/material_Received _Details.fxml"));
            Scene scene = new Scene(rootNode);
            Stage stage = (Stage) this.mainRoot.getScene().getWindow();
            stage.setTitle("Received Details");
            stage.setScene(scene);
            stage.centerOnScreen();
        } else {
            new Alert(Alert.AlertType.INFORMATION,"Not Added Materials !!").show();
        }
    }

    @FXML
    void btnIssueDetailsOnAction(ActionEvent event) throws IOException {

        setRawMaterialCount();
        if (rawCountItem > 0){
            Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/material_issue_details_form.fxml"));
            Scene scene = new Scene(rootNode);
            Stage stage = (Stage) this.mainRoot.getScene().getWindow();
            stage.setTitle("Issue Details");
            stage.setScene(scene);
            stage.centerOnScreen();
        } else {
            new Alert(Alert.AlertType.INFORMATION,"Not Added Materials !!").show();
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


    private void loadAddMaterialsForm() throws IOException {

        nodeAddmaterial = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/addMaterial_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(nodeAddmaterial);

    }


}
