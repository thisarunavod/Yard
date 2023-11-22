package lk.ijse.yard.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.dto.SupplierDto;
import lk.ijse.yard.model.MaterialModel;
import lk.ijse.yard.model.SupplierModel;

import java.io.IOException;
import java.sql.SQLException;

public class AddSupplierFormController {

    private final SupplierModel supplierModel = new SupplierModel();
    private Parent rootNodeSpecial;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtFieldCompanyName;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private TextField txtFieldSupplierId;

    @FXML
    void btnADDOnAction(ActionEvent event) {

        String supplierId = txtFieldSupplierId.getText();  if (!validText(supplierId)){ supplierId = null; }
        String companyName = txtFieldCompanyName.getText();  if (!validText(companyName)){ companyName = null; }
        String email = txtFieldEmail.getText();  if (!validText(email)){ email = null; }

        var dtoSupplier = new SupplierDto(supplierId,companyName,email);

        try {
            boolean isAddedSuppler = supplierModel.addSupplier(dtoSupplier);
            if (isAddedSuppler){
                new Alert(Alert.AlertType.CONFIRMATION,"Supplier Added Successfully !!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }


    }
    @FXML
    void btnMRDetailsOnAction(ActionEvent event) throws IOException {
        rootNodeSpecial = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/material_Received _Details.fxml"));

        Scene scene = new Scene(rootNodeSpecial);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setTitle("Received Details");
        stage.setScene(scene);
        stage.centerOnScreen();

    }

    private void clearFields() {
        txtFieldSupplierId.setText("");
        txtFieldCompanyName.setText("");
        txtFieldEmail.setText("");
    }

    private boolean validText(String text){
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' '){  return true;}
        }
        return false;
    }

}
