package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.SupplierDto;
import lk.ijse.yard.model.SupplierModel;

import java.sql.SQLException;
import java.util.List;

public class UpdateSupplierFormController {

    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    private JFXComboBox<String> cmbSupplierID;

    @FXML
    private TextField txtFieldNewCompanyName;

    @FXML
    private TextField txtFieldNewEmail;

    @FXML
    private TextField txtFieldnewID;

    @FXML
    void initialize(){
        loadAllSupplierIDs();
    }

    private void loadAllSupplierIDs() {

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

        String supplierId = cmbSupplierID.getValue();

        try {
            SupplierDto dto = supplierModel.findOldSupplierDetails(supplierId);
            if (dto != null){
                setOldDataforNewFields(dto);
            }else{
//                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void setOldDataforNewFields(SupplierDto dto) {
        txtFieldnewID.setText(dto.getSupplierId());
        txtFieldNewCompanyName.setText(dto.getCompanyName());
        txtFieldNewEmail.setText(dto.getEmail());
    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {

        String supplierId = txtFieldnewID.getText();  if (!validText(supplierId)){ supplierId = null; }
        String companyName = txtFieldNewCompanyName.getText();  if (!validText(companyName)){ companyName = null; }
        String email = txtFieldNewEmail.getText();  if (!validText(email)){ email = null; }


        try {
            boolean isUpdated = supplierModel.updateSupplierDetails(
                    new SupplierDto(
                            supplierId,
                            companyName,
                            email),cmbSupplierID.getValue()
            );
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Updated !!").show();
                loadAllSupplierIDs();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    private void clearFields() {
        txtFieldnewID.setText("");
        txtFieldNewCompanyName.setText("");
        txtFieldNewEmail.setText("");
    }

    private boolean validText(String text){
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' '){  return true;}
        }
        return false;
    }

}
