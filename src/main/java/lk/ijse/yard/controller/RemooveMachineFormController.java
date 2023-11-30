package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.yard.dto.MachineDto;
import lk.ijse.yard.model.MachineModel;
import lk.ijse.yard.model.MaterialModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class RemooveMachineFormController {
    private MachineModel machineModel = new MachineModel();

    @FXML
    private JFXComboBox<String> cmbMachineID;

    @FXML
    private Label lblMachineName;

    @FXML
    void initialize(){
        loadAllMachineID();
    }

    private void loadAllMachineID() {

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


    @FXML
    void btnOnActionRemoveMachine(ActionEvent event) {

        try {
            boolean isSelectID = validateMachine();
            if (isSelectID){
                String machineID = cmbMachineID.getValue();
                boolean isRemovedMachine = machineModel.removeMachine(machineID);
                if (isRemovedMachine){
                    new Alert(Alert.AlertType.INFORMATION,machineID+" is Remove on Yard !!").show(); loadAllMachineID();
                    cmbMachineID.getSelectionModel().clearSelection();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

    @FXML
    void cmbMachineIDOnAction(ActionEvent event) {

        String id = cmbMachineID.getValue();

        try {

            MachineDto dto = machineModel.findOldMaterialDetails(id);
            if (dto != null){
                lblMachineName.setText(dto.getMachineName());
            }else{
//                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean validateMachine() throws SQLException {

        if (cmbMachineID.getValue() == null){
            new Alert(Alert.AlertType.INFORMATION,"Not select Machine ID !!").show(); return false;
        }
        boolean isOutOfYard = machineModel.checkStatus(cmbMachineID.getValue());
        if (isOutOfYard){ new Alert(Alert.AlertType.ERROR,"This Machine On the Project !!").show(); return false ;}

        return true ;
    }
}
