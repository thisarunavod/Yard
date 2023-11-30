package lk.ijse.yard.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.Tm.MaterialTm;
import lk.ijse.yard.dto.Tm.VehicleListTm;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;

public class VehicleListFormController {

    private final VehicleModel vehicleModel = new VehicleModel();


    @FXML
    private TableColumn<?, ?> colEmpID;

    @FXML
    private TableColumn<?, ?> colEmpName;

    @FXML
    private TableColumn<?, ?> colVehicleID;

    @FXML
    private TableColumn<?, ?> colVehicleName;

    @FXML
    private TableView<VehicleListTm> tableVehicles;


    @FXML
    void  initialize(){

        loadAllVehicles();
        setCellValueFactory();

    }

    private void setCellValueFactory() {

        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("empID"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
    }

    private void loadAllVehicles() {

        try {
            List<VehicleDto> dtoList = vehicleModel.loadAllVehicles();
            ObservableList<VehicleListTm> obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {

                String empId = "";
                String empName = "";
                if (dtoList.get(i).getEmpId() == null) { empId = "NAV"; empName = "NAV"; }
                else{
                    empId = dtoList.get(i).getEmpId();
                    empName = EmployeeModel.getName(empId);
                }

                var tm = new VehicleListTm(
                        dtoList.get(i).getVehicleId(),
                        dtoList.get(i).getVehicleName(),
                        empId,
                        empName
                );

                obList.add(tm);
            }
            tableVehicles.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }



}
