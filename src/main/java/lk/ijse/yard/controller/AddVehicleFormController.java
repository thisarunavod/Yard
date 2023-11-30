package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.SupplierDto;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class AddVehicleFormController {

    private final VehicleModel vehicleModel = new VehicleModel();
    private final EmployeeModel employeeModel = new EmployeeModel();


    @FXML
    private JFXComboBox<String> cmbDrivwerID;

    @FXML
    private JFXTextArea txtFieldVehicleID;

    @FXML
    private JFXTextArea txtFieldVehicleName;

    @FXML
    void btnOnActionAddVehicle(ActionEvent event) {
        boolean isValidVehicle = validateVehicle();

        if (isValidVehicle){

            String vehicleID = txtFieldVehicleID.getText();
            String name = txtFieldVehicleName.getText();
            String empId = cmbDrivwerID.getValue();
            String rootStatus = "ON_yard";
            String driverAvailability = setDriverAvailability(empId);
            String removeOrWorking = "working";

            try {
                var vehicleDto = new VehicleDto(vehicleID , name , empId , rootStatus , driverAvailability , removeOrWorking);
                boolean isAddVehicle = vehicleModel.addVehicle(vehicleDto);
                if (isAddVehicle){ new Alert(Alert.AlertType.INFORMATION,"Vehicle Details Added Successfully !!").show(); clearFields();}

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private String setDriverAvailability(String empId) {

        String availability = null;
        if (empId == null){ return availability ;}
        else{
            try {  availability = employeeModel.getDriverAvailability(empId); return availability; }
            catch (SQLException e) { e.printStackTrace(); }
        }
        return availability;
    }

    private boolean validateVehicle() {
        String vehicleName = txtFieldVehicleName.getText();
        boolean isValidName = Pattern.matches("(?:\\d+\\s*)?[A-Za-z]+\\s*[A-Za-z]*\\s*[A-Za-z]*",vehicleName);
        if (!isValidName){ new Alert(Alert.AlertType.ERROR,"Invalid Vehicle Name").show(); return false; }

        String vehicleId = txtFieldVehicleID.getText();
        boolean isValidID = Pattern.matches("V\\d+",vehicleId);
        if (!isValidID){ new Alert(Alert.AlertType.ERROR,"Invalid Vehicle ID").show(); return false; }

        return true ;
    }

    @FXML
    void cmbDriverIDOnAction(ActionEvent event) {




    }

    @FXML
    void cmbDriverIDonActionMousePressed(MouseEvent event) {

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



    private void clearFields() {
        txtFieldVehicleID.setText("");
        cmbDrivwerID.getSelectionModel().clearSelection();
        txtFieldVehicleName.setText("");
    }
}
