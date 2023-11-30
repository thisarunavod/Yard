package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.model.EmployeeModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddEmployeeFormController {

    private ObservableList<String> jobTitle = FXCollections.observableArrayList();
    private final EmployeeModel employeeModel = new EmployeeModel();

    @FXML
    private JFXComboBox<String> cmbJobTitle;

    @FXML
    private JFXTextArea txtFieldEmpAddress;

    @FXML
    private JFXTextArea txtFieldEmpName;
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextArea txtFieldEmployeeID;

    @FXML
    void initialize(){
        setEmpJobTitleForCmbBox();
    }
    private void setEmpJobTitleForCmbBox() {
        jobTitle.add("DRIVER");
        jobTitle.add("LABOR");
        cmbJobTitle.setItems(jobTitle);
    }

    @FXML
    void cmbJobTitleOnAction(ActionEvent event) {
        if (cmbJobTitle.getValue().equals("DRIVER")){ txtFieldEmployeeID.setText("DR");} else { txtFieldEmployeeID.setText("LB"); }
    }


    @FXML
    void btnOnActionAddEmployee(ActionEvent event) throws IOException {

        boolean isValidEmployee = validEmpDetails();

        if (isValidEmployee){
            String jobTitle = cmbJobTitle.getValue();
            String empID = txtFieldEmployeeID.getText();
            String empName = txtFieldEmpName.getText();
            String address = txtFieldEmpAddress.getText();
            String availability = "AV";

            try {

                var empDto = new EmployeeDto(empID , empName , address , jobTitle , availability );
                boolean isAddEmployee = employeeModel.addEmployee(empDto);
                if (isAddEmployee){
                    new Alert(Alert.AlertType.INFORMATION,empDto.getEmpID()+" is Added Successfully !!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private void clearFields() {
        txtFieldEmpName.setText("");
        txtFieldEmployeeID.setText("");
        txtFieldEmpAddress.setText("");
    }

    private boolean validEmpDetails() {


        String Jobtitle = cmbJobTitle.getValue();
        if (Jobtitle == null){new Alert(Alert.AlertType.ERROR,"Not Select Job Title !!").show(); return false; }

        String empID = txtFieldEmployeeID.getText();
        boolean validEmpID = Pattern.matches("^(DR|LB)\\d+",empID);
        if (!validEmpID) { new Alert(Alert.AlertType.ERROR,"invalid Employee ID !!").show(); return false;}

        String empName = txtFieldEmpName.getText();
        boolean validName = Pattern.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+",empName);
        if (!validName){ new Alert(Alert.AlertType.ERROR,"invalid Employee Name !").show(); return false;}

        String address = txtFieldEmpAddress.getText();
        boolean validAddress = Pattern.matches("^[#.0-9a-zA-Z\\s,-]+$",address);
        if (!validAddress){ new Alert(Alert.AlertType.ERROR,"invalid Employee address !").show(); return false;}

        return true;
    }

}
