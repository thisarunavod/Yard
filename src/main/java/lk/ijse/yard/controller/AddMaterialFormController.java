package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.model.MaterialModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddMaterialFormController {



    @FXML
    private JFXComboBox<String> cmbBoxMaterialType;

    @FXML
    private TextField txtFieldMaterialId;

    @FXML
    private TextField txtFieldMaterialName;

    @FXML
    private TextField txtFieldQtyAvailable;

    @FXML
    private Label unitLabel;

    @FXML
    private AnchorPane root;

    private ObservableList<String> type = FXCollections.observableArrayList();

    @FXML
    void OnActionCmbType(ActionEvent event) {
        setUnitforMaterial(cmbBoxMaterialType.getValue());
    }

    private void setUnitforMaterial(String value) {
        if (value.equals("AGGREGATE")) { unitLabel.setText("Cube"); }
        if (value.equals("SAND")) { unitLabel.setText("Cube");}
        if (value.equals("CEMENTS")) { unitLabel.setText("ton");}
        if (value.equals("REINFORCEMENTS")) { unitLabel.setText("ton");}
        if (value.equals("PLYWOOD")) { unitLabel.setText("Sq.m");}
    }



    @FXML
    void initialize(){
        setMaterialsTypeForCmbBox();
    }

    @FXML
    void btnADDOnAction(ActionEvent event) {

        String materialId = txtFieldMaterialId.getText();   if (!validText(materialId)){ materialId = null; }
        String materialName = txtFieldMaterialName.getText();  if (!validText(materialName)){ materialName = null; }
        String type = cmbBoxMaterialType.getValue();  if(type == null){ new Alert(Alert.AlertType.INFORMATION,"Please select type !!").show();}

        double qtyAvailable = -1;

        try { qtyAvailable = Double.parseDouble(txtFieldQtyAvailable.getText()); }
        catch (NumberFormatException e) {
            new Alert(Alert.AlertType.INFORMATION,"not valid qty !! ").show();
        }

        String unit = unitLabel.getText();

        if (type != null && qtyAvailable >=0   ){

            var dtoMaterial = new MaterialDto(materialId,materialName,type,qtyAvailable,unit);
            var modelMaterial = new MaterialModel();
            try {
                boolean isAdd = modelMaterial.addMatrials(dtoMaterial);
                if (isAdd){
                    new Alert(Alert.AlertType.CONFIRMATION,"Material Added Succesfully !! ").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }
    @FXML
    void btnMRDetailsOnAction(ActionEvent event) throws IOException {
            Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/material_Received _Details.fxml"));
            Scene scene = new Scene(rootNode);
            Stage stage = (Stage) this.root.getScene().getWindow();
            stage.setTitle("Received Details");
            stage.setScene(scene);
            stage.centerOnScreen();
    }

    private void setMaterialsTypeForCmbBox(){
        type.add("AGGREGATE");
        type.add("SAND");
        type.add("CEMENTS");
        type.add("REINFORCEMENTS");
        type.add("PLYWOOD");
        cmbBoxMaterialType.setItems(type);
    }

    private void clearFields(){
        txtFieldMaterialId.setText("");
        txtFieldMaterialName.setText("");
        txtFieldQtyAvailable.setText("");
        unitLabel.setText("");
    }

    private boolean validText(String text){
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' '){  return true;}
        }
        return false;
    }
}
