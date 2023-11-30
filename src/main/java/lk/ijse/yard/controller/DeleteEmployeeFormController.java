package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class DeleteEmployeeFormController {

    private final EmployeeModel employeeModel = new EmployeeModel();
    private final VehicleModel vehicleModel = new VehicleModel();

    @FXML
    private JFXComboBox<String> cmbEmployeeID;

    @FXML
    private Label lblEmployeeAddress;

    @FXML
    private Label lblEmployeeName;

    @FXML
    void initialize(){

        loadAllEmployeIDs();

    }

    private void loadAllEmployeIDs() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> employeeDtoList = employeeModel.loadAllEmployees();
            if (employeeDtoList != null){
                for (int i = 0; i < employeeDtoList.size(); i++) {
                    obList.add(employeeDtoList.get(i).getEmpID());
                }
                cmbEmployeeID.setItems(obList);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }


    @FXML
    void btnOnActionDeleteEmployee(ActionEvent event) {

        boolean isSelectId = valiEmpID();

        if (isSelectId){

            String empId = cmbEmployeeID.getValue();
            try {

                boolean isVehicleRegisterDriver = vehicleModel.isVehicleRegisterDriver(empId);

                if (isVehicleRegisterDriver){
                    boolean deleteEmploye = registerDriverDeletionProcess(empId);
                    if (deleteEmploye){ new Alert(Alert.AlertType.INFORMATION,"Employee Deleted !!").show(); loadAllEmployeIDs(); clearFields();}
                }else{
                    boolean deleteEmploye = employeeModel.deleteEmployee(empId);
                    if (deleteEmploye){ new Alert(Alert.AlertType.INFORMATION,"Employee Deleted !!").show(); loadAllEmployeIDs();}
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private void clearFields() {
        cmbEmployeeID.getSelectionModel().clearSelection();
        lblEmployeeName.setText("");
        lblEmployeeAddress.setText("");
    }

    private boolean registerDriverDeletionProcess(String empId) {
        boolean isDelete = false;
        try {
             isDelete = employeeModel.deleteRegisterDriverse(empId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDelete;
    }



    @FXML
    void cmbEmployeIDOnAction(ActionEvent event) {

        String empId = cmbEmployeeID.getValue();

        try {
            var employeDto = employeeModel.getEmployeeDetails(empId);
            lblEmployeeName.setText(employeDto.getEmpName());
            lblEmployeeAddress.setText(employeDto.getEmpAddress());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean valiEmpID() {

        String empId = cmbEmployeeID.getValue();
        if (empId == null){ new Alert(Alert.AlertType.ERROR,"Not Select Employee ID").show(); return false; }

        return true ;
    }

}
