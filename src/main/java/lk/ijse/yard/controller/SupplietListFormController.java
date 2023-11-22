package lk.ijse.yard.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.SupplierDto;
import lk.ijse.yard.dto.Tm.MaterialTm;
import lk.ijse.yard.dto.Tm.SupplierTm;
import lk.ijse.yard.model.SupplierModel;

import java.sql.SQLException;
import java.util.List;


public class SupplietListFormController {

    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    private TableColumn<?, ?> colCompanyName;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableView<SupplierTm> tableSupplierList;

    @FXML
    void initialize(){

        loadAllSupplierDetails();
        setCellValueFactory();

    }
    private void setCellValueFactory() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllSupplierDetails() {

        try {
            List<SupplierDto> dtoList = supplierModel.loadAllSuppliers();
            ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {

                var tm = new SupplierTm(
                        dtoList.get(i).getSupplierId(),
                        dtoList.get(i).getCompanyName(),
                        dtoList.get(i).getEmail()
                );
                obList.add(tm);
            }
            tableSupplierList.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
