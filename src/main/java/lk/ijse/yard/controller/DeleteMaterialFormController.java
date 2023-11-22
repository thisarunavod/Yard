package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.model.MaterialModel;

import java.sql.SQLException;
import java.util.List;

public class DeleteMaterialFormController {
    private final MaterialModel materialModel = new MaterialModel();
    private boolean deletreMaterialIdOncmbBox = false ;

    @FXML
    private JFXComboBox<String> cmbMaterialID;

    @FXML
    private Label lblMaterialName;

    @FXML
    private Label lblMaterialQty;

    @FXML
    private Label lblMaterialType;

    @FXML
    private Label lblUnit;

    @FXML
    void initialize(){
        loadAllMaterialIDs();
    }

    private void loadAllMaterialIDs(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<MaterialDto> materialList = materialModel.loadAllMaterials();
            for (int i = 0; i < materialList.size(); i++) {

                obList.add(materialList.get(i).getMaterialID());

            }
            cmbMaterialID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void OnActioncmbIDSelect(ActionEvent event) {

        String materialId = cmbMaterialID.getValue();
        try {

            MaterialDto dto = materialModel.findOldMaterialDetails(materialId);
            if (dto != null){
                setOldDataforNewFields(dto);
            }else if(!deletreMaterialIdOncmbBox){
                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void setOldDataforNewFields(MaterialDto dto) {
        lblMaterialName.setText(dto.getMaterialName());
        lblMaterialType.setText(dto.getType());
        lblMaterialQty.setText(String.valueOf(dto.getQtyAvailable()));
        lblUnit.setText(dto.getUnit());
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {

        String materialId = cmbMaterialID.getValue();
        if (materialId != null){
            try {
                boolean isDeleted = materialModel.deleteMaterial(materialId);
                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"Material Deleted Succesfully !!").show();
                    deletreMaterialIdOncmbBox = true;
                    loadAllMaterialIDs();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Not seleted ID !!").show();
        }

    }



}
