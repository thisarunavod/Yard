package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import jdk.jshell.spi.SPIResolutionException;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.SupplierDto;
import lk.ijse.yard.model.SupplierModel;

import java.sql.SQLException;
import java.util.AbstractList;
import java.util.List;

public class DeleteSupplierFormController {

    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    private JFXComboBox<String> cmbSupplierID;

    @FXML
    private Label lblCompanyEmail;

    @FXML
    private Label lblCompanyName;

    @FXML
    private Label lblMaterialQty;


    @FXML
    void initialize(){
        loadAllMaterialIDs();
    }

    private void loadAllMaterialIDs(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<SupplierDto> supplierDtoList = supplierModel.loadAllSuppliers();
            for (int i = 0; i < supplierDtoList.size(); i++) {
                obList.add(supplierDtoList.get(i).getSupplierId());
            }
            cmbSupplierID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void OnActioncmbIDSelect(ActionEvent event) {

        String materialId = cmbSupplierID.getValue();

        try {

            SupplierDto dto = supplierModel.findOldSupplierDetails(materialId);
            if (dto != null){
                setOldDataforNewFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION,"Supplier Not found !!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void setOldDataforNewFields(SupplierDto dto) {
        lblCompanyName.setText(dto.getCompanyName());
        lblCompanyEmail.setText(dto.getEmail());
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {
        String supplierId = cmbSupplierID.getValue();

        try {
            boolean isDeleted = supplierModel.deleteSupplier(supplierId);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted Successfully").show();
                loadAllMaterialIDs();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }


    }


}
