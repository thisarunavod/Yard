package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.model.MaterialModel;

import java.sql.SQLException;
import java.util.List;

public class UpdateMaterialFormController {

    private final MaterialModel materialModel = new MaterialModel();

    @FXML
    private JFXComboBox<String> cmbMaterialID;

    @FXML
    private JFXComboBox<String> cmbMaterialType;

    @FXML
    private TextField txtFieldnewID;

    @FXML
    private TextField txtFieldNewName;

    @FXML
    private TextField txtFieldNewQtyAvailable;

    @FXML
    private Label labelUnit;



    @FXML
    void initialize(){
        loadAllMaterialIDs();
    }

    @FXML
    void cmbBoxOnActionSelectType(ActionEvent event) {
        try {
            setUnitforMaterial(cmbMaterialType.getValue());

        } catch (Exception e) {

        }
    }


    @FXML
    void OnActioncmbIDSelect(ActionEvent event) {

        String materialId = cmbMaterialID.getValue();
        try {

            MaterialDto dto = materialModel.findOldMaterialDetails(materialId);
            if (dto != null){
                setOldDataforNewFields(dto);
            }else{
//                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void txtnewIDOnAction(ActionEvent event) {}

    private void setOldDataforNewFields(MaterialDto dto) {
        txtFieldnewID.setText(dto.getMaterialID());
        txtFieldNewName.setText(dto.getMaterialName());
        txtFieldNewQtyAvailable.setText(String.valueOf(dto.getQtyAvailable()));
        setOldTypeforCmbBox(dto);
        labelUnit.setText(dto.getUnit());

    }
    private void setOldTypeforCmbBox(MaterialDto dto) {
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("AGGREGATE");
        type.add("SAND");
        type.add("CEMENTS");
        type.add("REINFORCEMENTS");
        type.add("PLYWOOD");
        cmbMaterialType.setItems(type);
        cmbMaterialType.setValue(dto.getType());

    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {
        String materialId = txtFieldnewID.getText(); if (!validText(materialId)){ materialId = null; }
        String materialName = txtFieldNewName.getText();  if (!validText(materialName)){ materialName = null; }
        String type = cmbMaterialType.getValue();  if(type == null){ new Alert(Alert.AlertType.INFORMATION,"Please select type !!").show();}
        double qtyAvailable = -1;
        String unit = labelUnit.getText();
        try { qtyAvailable = Double.parseDouble(txtFieldNewQtyAvailable.getText()); }
        catch (NumberFormatException e) {
            new Alert(Alert.AlertType.INFORMATION,"not valid qty !! ").show();
        }

        if (type != null && qtyAvailable >=0   ){
            try {
                boolean isUpdated = materialModel.updateMaterialDetails(
                        new MaterialDto(
                                materialId,
                                materialName,
                                type,
                                qtyAvailable,
                                unit),cmbMaterialID.getValue()
                );
                if (isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"Successfully Updated !!").show();
                    loadAllMaterialIDs();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }

        }
    }
    private boolean validText(String text){
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' '){  return true;}
        }
        return false;
    }

    private void setUnitforMaterial(String value) {
        if (value.equals("AGGREGATE")) { labelUnit.setText("Cube"); }
        if (value.equals("SAND")) { labelUnit.setText("Cube");}
        if (value.equals("CEMENTS")) { labelUnit.setText("ton");}
        if (value.equals("REINFORCEMENTS")) { labelUnit.setText("ton");}
        if (value.equals("PLYWOOD")) { labelUnit.setText("Sq.m");}
    }

    private void loadAllMaterialIDs(){

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List< MaterialDto > materialList = materialModel.loadAllMaterials();
            for (int i = 0; i < materialList.size(); i++) {

                obList.add(materialList.get(i).getMaterialID());

            }
            cmbMaterialID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
