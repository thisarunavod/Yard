package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;


public class UpdateVehicleFormController {

    private  final VehicleModel vehicleModel = new VehicleModel();
    private  final EmployeeModel employeeModel = new EmployeeModel();

    @FXML
    private JFXComboBox<String> cmbDrivwerID;

    @FXML
    private JFXComboBox<String> cmbVehicleID;

    @FXML
    private JFXTextArea txtFieldVehicleName;

    @FXML
    private Label lblDriverStatus;



    @FXML
    void initialize(){

        loadAllVehicleIDs();
        cmbDrivwerID.setDisable(true);

    }

    private void loadAllVehicleIDs() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<VehicleDto> vehicleDtoList = vehicleModel.loadAllVehicleIDs();
            for (int i = 0; i < vehicleDtoList.size(); i++) {
                obList.add(vehicleDtoList.get(i).getVehicleId());
            }
            cmbVehicleID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void cmbVehicleIDOnAction(ActionEvent event) {
        String vehicleID = cmbVehicleID.getValue();

        try {
            String vehicleName = vehicleModel.getVehicleName(vehicleID);
            txtFieldVehicleName.setText(vehicleName);
            boolean haveDriver = checkAddDriver(vehicleID);



            if (!haveDriver){ getAvailableDriversIDs(); lblDriverStatus.setText("Not Added Driver"); }
            else{
                lblDriverStatus.setText("Driver Already Added");
                cmbDrivwerID.getSelectionModel().clearSelection();
                cmbDrivwerID.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnOnActionUpdateVehicleDetails(ActionEvent event) {

        boolean isvalidVehicleDetails = validateVehicle();

        if (isvalidVehicleDetails){

            String driverId = cmbDrivwerID.getValue();
            if (driverId != null){  updateVehicleWithSetDriverID(); clearFields();  }
            else { updateVehicleNormal(); clearFields(); }

        }
    }

    private void clearFields() {
        cmbVehicleID.getSelectionModel().clearSelection();
        cmbDrivwerID.getSelectionModel().clearSelection();
        txtFieldVehicleName.setText("");
    }

    private void updateVehicleNormal() {
        String vehicleID = cmbVehicleID.getValue();
        String name = txtFieldVehicleName.getText();
        String driverId = null;

        try {
            var vehicleDto  = new VehicleDto(vehicleID,name,driverId,"ON_yard",null,"working");
            boolean isUpdated = vehicleModel.updateVehicleNormal(vehicleDto);
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Vehicle details Successfully Updated !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void updateVehicleWithSetDriverID() {

        String vehicleID = cmbVehicleID.getValue();
        String name = txtFieldVehicleName.getText();
        String driverId = cmbDrivwerID.getValue();
        String availability = "";
        try {
             availability = employeeModel.getDriverAvailability(driverId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            var vehicleDto  = new VehicleDto(vehicleID,name,driverId,"ON_yard",availability,"working");
            boolean isUpdated = vehicleModel.updateVehicleWithDriver(vehicleDto);
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Vehicle details Successfully Updated !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void cmbDriverIDOnAction(ActionEvent event) {



    }

    private void getAvailableDriversIDs() {

        cmbDrivwerID.setDisable(false);
        loadAvailableDriverIDs();

    }
    private void loadAvailableDriverIDs() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> employeeDtoList = vehicleModel.loadAllAvailableDriverse();
            if (employeeDtoList != null){
                for (int i = 0; i < employeeDtoList.size(); i++) {
                    obList.add(employeeDtoList.get(i).getEmpID());
                }
                cmbDrivwerID.setItems(obList);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean checkAddDriver(String vehicleID) {
        boolean isAddedDriver = false;
        try {
            String id  = vehicleModel.getAddedDriverID(vehicleID);
            System.out.println(id);
            if (id != null){ isAddedDriver = true ; }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAddedDriver;
    }

    private boolean validateVehicle() {
        String vehicleName = txtFieldVehicleName.getText();
        boolean isValidName = Pattern.matches("(?:\\d+\\s*)?[A-Za-z]+\\s*[A-Za-z]*\\s*[A-Za-z]*",vehicleName);
        if (!isValidName){ new Alert(Alert.AlertType.ERROR,"Invalid Vehicle Name").show(); return false; }

        String vehicleId = cmbVehicleID.getValue();
        if (vehicleId == null){ new Alert(Alert.AlertType.ERROR,"Not Select Vehicle ID").show(); return false; }

        return true ;
    }

    @FXML
    void cmbDriverIDonActionMousePressed(MouseEvent event) {

    }

}
