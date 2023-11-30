package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lk.ijse.yard.dto.MachineDto;
import lk.ijse.yard.model.MachineModel;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddMachineFormController {

    private MachineModel machineModel = new MachineModel();
    @FXML
    private JFXTextArea txtFieldMachineID;

    @FXML
    private JFXTextArea txtFieldMachineName;

    @FXML
    void initialize(){
        txtFieldMachineID.setText("MA");
    }


    @FXML
    void btnOnActionAddMachine(ActionEvent event) {

        boolean isValidateMachineDetails = validateMachine();

        if (isValidateMachineDetails){
            String machineID = txtFieldMachineID.getText();
            String machineName = txtFieldMachineName.getText();
            try {
                var machineDto = new MachineDto(machineID , machineName , "ON_yard");
                boolean isAddedMachine = machineModel.addMachine(machineDto);
                if (isAddedMachine) { new Alert(Alert.AlertType.INFORMATION,machineID+" is Added Successfully !!").show(); clearFields(); }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }

        }
    }

    private void clearFields() {
        txtFieldMachineID.setText("MA");
        txtFieldMachineName.setText("");
    }

    private boolean validateMachine() {

        String machineId = txtFieldMachineID.getText();
        boolean isValidID = Pattern.matches("MA\\d+",machineId);
        if (!isValidID){ new Alert(Alert.AlertType.ERROR,"Invalid Machine ID").show(); return false; }

        String machineName = txtFieldMachineName.getText();
        boolean isValidName = Pattern.matches("(?:\\d+\\s*)?[A-Za-z]+\\s*[A-Za-z]*\\s*[A-Za-z]*",machineName);
        if (!isValidName){ new Alert(Alert.AlertType.ERROR,"Invalid Machine Name").show(); return false; }

        return true ;
    }

}
