package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class RemoveVehicleFormController {

    private final VehicleModel vehicleModel = new VehicleModel();

    @FXML
    private JFXComboBox<String> cmbVehicleID;

    @FXML
    private Label lblVehicleName;

    @FXML
    void initialize(){  loadAllVehicleIDs(); }

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
    void btnOnActionRemoveVehicle(ActionEvent event) {
        boolean isSelectID = validateVehicle();

        if (isSelectID){

            String vehicleID = cmbVehicleID.getValue();

            try {
                boolean vehicleOnRoot = vehicleModel.checkVehicleOnRoot(vehicleID);
                if (!vehicleOnRoot){
                    boolean isRemove = vehicleModel.removeVehicle(vehicleID);
                    if (isRemove){
                        new Alert(Alert.AlertType.INFORMATION,vehicleID + " is Remove from Yard !").show();
                        loadAllVehicleIDs();
                    }
                }else{
                    new Alert(Alert.AlertType.ERROR,vehicleID + " is On Root !").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }

    }

    @FXML
    void cmbEmployeIDOnAction(ActionEvent event) {

        String vehicleId = cmbVehicleID.getValue();
        try {

            String name = vehicleModel.getVehicleName(vehicleId);
            lblVehicleName.setText(name);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean validateVehicle() {

        String vehicleId = cmbVehicleID.getValue();
        if (vehicleId == null){ new Alert(Alert.AlertType.ERROR,"Not Select Vehicle ID").show(); return false; }

        return true ;
    }


}
