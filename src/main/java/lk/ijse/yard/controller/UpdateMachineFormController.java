package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import lk.ijse.yard.dto.MachineDto;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.MachineModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class UpdateMachineFormController {

    private MachineModel machineModel = new MachineModel();

    @FXML
    private JFXComboBox<String> cmbMachineID;

    @FXML
    private JFXTextArea txtFieldMachineName;

    @FXML
    private JFXTextArea txtFieldNewMachineID;

    @FXML
    void btnOnActionUpdateMachine(ActionEvent event) {

        boolean isValidMachineDetails = validateMachine();
        if (isValidMachineDetails){
            try {
                String machineID = txtFieldNewMachineID.getText();
                String machineName = txtFieldMachineName.getText();

                boolean isUpdated = machineModel.updateMachineDetails(
                        new MachineDto( machineID, machineName),
                        cmbMachineID.getValue()
                );

                if (isUpdated){
                    new Alert(Alert.AlertType.INFORMATION,"Successfully Updated !!").show();
                    loadAllMachineIDs();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }

        }

    }

    private void clearFields() {
        txtFieldNewMachineID.setText("");
        txtFieldMachineName.setText("");
    }

    @FXML
    void cmbMachineIDOnAction(ActionEvent event) {

        String machineId = cmbMachineID.getValue();
        try {

            MachineDto dto = machineModel.findOldMaterialDetails(machineId);
            if (dto != null){
                setOldDataforNewFields(dto);
            }else{
//                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void setOldDataforNewFields(MachineDto dto) {
        txtFieldNewMachineID.setText(dto.getMachineID());
        txtFieldMachineName.setText(dto.getMachineName());
    }

    @FXML
    void cmbMachineIDonActionMousePressed(MouseEvent event) {

        loadAllMachineIDs();

    }
    private void loadAllMachineIDs() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<MachineDto> machineDtoList = machineModel.loadAllMachines();
            for (int i = 0; i < machineDtoList.size(); i++) {
                obList.add(machineDtoList.get(i).getMachineID());
            }
            cmbMachineID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateMachine() {

        String machineId = txtFieldNewMachineID.getText();
        boolean isValidID = Pattern.matches("MA\\d+",machineId);
        if (!isValidID){ new Alert(Alert.AlertType.ERROR,"Invalid Machine ID").show(); return false; }

        String machineName = txtFieldMachineName.getText();
        boolean isValidName = Pattern.matches("(?:\\d+\\s*)?[A-Za-z]+\\s*[A-Za-z]*\\s*[A-Za-z]*",machineName);
        if (!isValidName){ new Alert(Alert.AlertType.ERROR,"Invalid Machine Name").show(); return false; }

        return true ;
    }


}
