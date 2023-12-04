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

        boolean isValidMaterialDetails = validMaterialsDetails();


        if (isValidMaterialDetails){
            String materialId = txtFieldMaterialId.getText();
            String materialName = txtFieldMaterialName.getText();
            String type = cmbBoxMaterialType.getValue();
            double qty = 0.0;
            try {
                 qty = Double.parseDouble(txtFieldQtyAvailable.getText());
            } catch (NumberFormatException e) { }

            String unit = unitLabel.getText();

            var dtoMaterial = new MaterialDto(materialId,materialName,type,qty,unit);
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


    private boolean validMaterialsDetails() {


        String materialID  = txtFieldMaterialId.getText();
        boolean ValidMaterialID = Pattern.matches("^[A-Za-z]{2,}[0-9]{3,}$",materialID);
        if (!ValidMaterialID) { new Alert(Alert.AlertType.ERROR,"Invalid Material ID !!").show(); return false;}

        String materialName = txtFieldMaterialName.getText();
        boolean validMaterialName = Pattern.matches("^[#.0-9a-zA-Z\\s,-]+$",materialName);
        if (!validMaterialName) { new Alert(Alert.AlertType.ERROR,"Invalid Material Name !!").show(); return false;}

        if (cmbBoxMaterialType.getValue() == null){
            new Alert(Alert.AlertType.ERROR,"Not Select Material Type !!").show();
            return false;
        }

        String qty = txtFieldQtyAvailable.getText();
        boolean validQty = Pattern.matches("^[0-9]+(?:\\.[0-9]+)?$",qty);
        if (!validQty){ new Alert(Alert.AlertType.ERROR,"invalid Employee address !").show(); return false;}


        return true;

    }
}
