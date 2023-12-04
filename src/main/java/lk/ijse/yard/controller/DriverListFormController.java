package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.Tm.DriverTm;
import lk.ijse.yard.dto.Tm.MaterialTm;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;

public class DriverListFormController {

    private final EmployeeModel employeeModel = new EmployeeModel();
    private final VehicleModel vehicleModel = new VehicleModel();

    @FXML
    private TableColumn<?, ?> colDriverAvailability;

    @FXML
    private TableColumn<?, ?> colDriverID;

    @FXML
    private TableColumn<?, ?> colDriverName;

    @FXML
    private TableView<DriverTm> tblDriver;

    @FXML
    void initialize(){

        loadAllDrivers();
        setCellValueFactory();

    }

    private void setCellValueFactory() {

        colDriverID.setCellValueFactory(new PropertyValueFactory<>("driverID"));
        colDriverName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        colDriverAvailability.setCellValueFactory(new PropertyValueFactory<>("btn"));


    }

    private void loadAllDrivers() {

        try {

            ObservableList<DriverTm> obList = FXCollections.observableArrayList();
            List<EmployeeDto> dtoList = employeeModel.loadAllDriverse();

            for (int i = 0; i < dtoList.size(); i++) {

                JFXToggleButton btn = new JFXToggleButton();
                btn.setCursor(Cursor.HAND);
                boolean isVehicleRegisterDriver = vehicleModel.isVehicleRegisterDriver(dtoList.get(i).getEmpID());

                if (isVehicleRegisterDriver){
                    boolean vehicleInYard  = VehicleModel.isOnYard(dtoList.get(i).getEmpID());
                    if (vehicleInYard){

                        if (dtoList.get(i).getAvailability().equals("NA")){  btn.setSelected(false); btn.setText(" is OUT");} else { btn.setSelected(true); btn.setText(" is IN"); }


                        var dto = dtoList.get(i);
                        btn.setOnAction((e) ->{

                            if (btn.isSelected()){
                                try {
                                    boolean changeAv = employeeModel.changeAvailability(dto , "AV");
                                    if (changeAv) { btn.setText("is IN");}
                                } catch (SQLException throwables) { throwables.printStackTrace();}
                            }else{
                                try {
                                    boolean changeAv = employeeModel.changeAvailability(dto,"NA");
                                    if (changeAv) { btn.setText("is OUT"); }
                                } catch (SQLException throwables) { throwables.printStackTrace(); }
                            }

                        });
                    } else {
                        btn.setText("ON Root");
                        btn.setSelected(true);
                        btn.setDisable(true);
                    }

                }else{

                    if (dtoList.get(i).getAvailability().equals("NA")){  btn.setSelected(false); btn.setText(" is OUT");} else { btn.setSelected(true); btn.setText(" is IN"); }

                    var dto = dtoList.get(i);
                    btn.setOnAction((e) ->{

                        if (btn.isSelected()){
                            try {
                                boolean changeAv = employeeModel.changeAvailability(dto , "AV");
                                if (changeAv) { btn.setText("is IN");}
                            } catch (SQLException throwables) { throwables.printStackTrace();}
                        }else{
                            try {
                                boolean changeAv = employeeModel.changeAvailability(dto,"NA");
                                if (changeAv) { btn.setText("is OUT"); }
                            } catch (SQLException throwables) { throwables.printStackTrace(); }
                        }

                    });
                }

                var tm = new DriverTm(
                        dtoList.get(i).getEmpID(),
                        dtoList.get(i).getEmpName(),
                        btn
                );

                obList.add(tm);
            }

            tblDriver.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

}
