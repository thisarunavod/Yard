package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.dto.MachineDto;
import lk.ijse.yard.dto.Tm.MachineListTm;
import lk.ijse.yard.dto.Tm.VehicleListTm;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.MachineModel;

import java.sql.SQLException;
import java.util.List;

public class MachineListFormController {
    private MachineModel machineModel = new MachineModel();

    @FXML
    private TableColumn<?, ?> colMachineID;

    @FXML
    private TableColumn<?, ?> colMachineName;

    @FXML
    private TableColumn<?, ?> colMachineStatus;

    @FXML
    private TableView<MachineListTm> tableMachines;

    @FXML
    void  initialize(){
        loadAllMachines();
        setCellValueFactory();

    }

    private void setCellValueFactory() {
        colMachineID.setCellValueFactory(new PropertyValueFactory<>("machineID"));
        colMachineName.setCellValueFactory(new PropertyValueFactory<>("machineName"));
        colMachineStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAllMachines() {
        try {
            List<MachineDto> dtoList = machineModel.loadAllMachines();

            ObservableList<MachineListTm> obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {

                JFXToggleButton btnStatus = new JFXToggleButton();
                if (dtoList.get(i).getStatus().equals("ON_yard")){  btnStatus.setSelected(false); btnStatus.setText(" ON Yard");} else { btnStatus.setSelected(true); btnStatus.setText("ON project"); }

                var dto = dtoList.get(i);
                btnStatus.setOnAction((e) ->{
                    if (btnStatus.isSelected()){
                        try {
                            boolean changestatus = machineModel.changeStatus(dto , "ON_project");
                            if (changestatus) { btnStatus.setText("ON project");}
                        } catch (SQLException throwables) { throwables.printStackTrace();}
                    }else{
                        try {
                            boolean changestatus = machineModel.changeStatus(dto,"ON_yard");
                            if (changestatus) { btnStatus.setText("ON yard"); }
                        } catch (SQLException throwables) { throwables.printStackTrace(); }
                    }
                });


                var tm = new MachineListTm(
                        dtoList.get(i).getMachineID(),
                        dtoList.get(i).getMachineName(),
                        btnStatus
                );

                obList.add(tm);
            }
            tableMachines.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

}
