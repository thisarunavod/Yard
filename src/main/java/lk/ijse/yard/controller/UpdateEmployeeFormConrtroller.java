package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.MaterialModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class UpdateEmployeeFormConrtroller {
    private final EmployeeModel employeeModel = new EmployeeModel();

    @FXML
    private JFXComboBox<String> cmbEmployeeID;

    @FXML
    private Label lblJTitle;

    @FXML
    private JFXTextArea txtFieldEmpAddress;

    @FXML
    private JFXTextArea txtFieldEmpName;

    @FXML
    void initialize(){

        loadAllEmployeeIDs();

    }

    private void loadAllEmployeeIDs() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> employeeDtoList = employeeModel.loadAllEmployees();
            for (int i = 0; i < employeeDtoList.size(); i++) {

                obList.add(employeeDtoList.get(i).getEmpID());

            }
            cmbEmployeeID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void cmbIDOnAction(ActionEvent event) {
        String empID = cmbEmployeeID.getValue();

        try {

            EmployeeDto dto = employeeModel.findOldEmployeeDetails(empID);
            if (dto != null){
                setOldDataforNewFields(dto);
            }else{
//                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void setOldDataforNewFields(EmployeeDto dto) {

        txtFieldEmpName.setText(dto.getEmpName());
        txtFieldEmpAddress.setText(dto.getEmpAddress());
        lblJTitle.setText(dto.getJobTitle());

    }


    @FXML
    void btnOnActionUpdateEmployee(ActionEvent event) {

        boolean isValidEmployee = validEmpDetails();

        if (isValidEmployee){

            String empId = cmbEmployeeID.getValue();
            String jobTitle = lblJTitle.getText();
            String empName = txtFieldEmpName.getText();
            String empAddress = txtFieldEmpAddress.getText();
            String availability = "NA";

            try {
                var employeeDto = new EmployeeDto(empId , empName , empAddress , jobTitle , availability);
                boolean isUpdated = employeeModel.updateEmployee(employeeDto);
                if (isUpdated){
                    new Alert(Alert.AlertType.INFORMATION,"Successfully Updated !").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }

        }

    }

    private boolean validEmpDetails() {

        String empID = cmbEmployeeID.getValue();
        if (empID == null){new Alert(Alert.AlertType.ERROR,"Not Select Employee ID !!").show(); return false; }

        String empName = txtFieldEmpName.getText();
        boolean validName = Pattern.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+",empName);
        if (!validName){ new Alert(Alert.AlertType.ERROR,"invalid Employee Name !").show(); return false;}

        String address = txtFieldEmpAddress.getText();
        boolean validAddress = Pattern.matches("^[#.0-9a-zA-Z\\s,-]+$",address);
        if (!validAddress){ new Alert(Alert.AlertType.ERROR,"invalid Employee address !").show(); return false;}

        return true;
    }


}
