package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.dto.Tm.AvailbleVehicleTm;
import lk.ijse.yard.dto.Tm.VehicleListTm;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DailyAvailableVehicleListController {

    private final VehicleModel vehicleModel = new VehicleModel();

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private TableColumn<?, ?> colEmpID;

    @FXML
    private TableColumn<?, ?> colEmpName;

    @FXML
    private TableColumn<?, ?> colRootStatus;

    @FXML
    private TableColumn<?, ?> colVehicleID;

    @FXML
    private TableColumn<?, ?> colVehicleName;

    @FXML
    private TableView<AvailbleVehicleTm> tableVehicleList;


    @FXML
    void initialize() throws IOException {

        loadAllDriverAvailbleVehicles();
        setCellValueFactory();

    }

    private void setCellValueFactory() {
        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("empID"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colRootStatus.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }


    private void loadAllDriverAvailbleVehicles() {

        try {
            List<VehicleDto> dtoList = vehicleModel.loadAllDriverAvailbleVehicles();
            ObservableList<AvailbleVehicleTm> obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {

                String empName = EmployeeModel.getName(dtoList.get(i).getEmpId());

                JFXToggleButton btn = new JFXToggleButton();
                if (VehicleModel.getRootStatus(dtoList.get(i).getEmpId()).equals("ON_yard")){ btn.setSelected(false); btn.setText("ON Yard");}
                else { btn.setSelected(true); btn.setText("ON Root");  }

                int finalI = i;
                btn.setOnAction((e) ->{
                    if (btn.isSelected()){
                        try {
                            if(VehicleModel.changeRootStatus(dtoList.get(finalI).getEmpId(),"ON_root")){btn.setText("ON Root");}
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    else {
                        try {
                            if(VehicleModel.changeRootStatus(dtoList.get(finalI).getEmpId(),"ON_yard")){btn.setText("ON Yard");}
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });

                var tm = new AvailbleVehicleTm(
                        dtoList.get(i).getVehicleId(),
                        dtoList.get(i).getVehicleName(),
                        dtoList.get(i).getEmpId(),
                        empName,
                        btn
                );

                obList.add(tm);
            }
            tableVehicleList.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }





    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/vehicles/vehicleManage_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Vehicle Manage");
        stage.setScene(scene);
        stage.centerOnScreen();

    }


}
