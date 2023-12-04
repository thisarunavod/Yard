package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.Tm.DriverTm;
import lk.ijse.yard.dto.Tm.LaborTm;
import lk.ijse.yard.model.EmployeeModel;
import lk.ijse.yard.model.VehicleModel;

import java.sql.SQLException;
import java.util.List;

public class LaborListFormController {

    private final EmployeeModel employeeModel = new EmployeeModel();

    @FXML
    private TableColumn<?, ?> colLaborAvailability;

    @FXML
    private TableColumn<?, ?> colLaborID;

    @FXML
    private TableColumn<?, ?> colLaborName;

    @FXML
    private TableView<LaborTm> tblLabor;


    @FXML
    void initialize(){
        loadAllLabors();
        setCellValueFactory();
    }
    private void setCellValueFactory() {

        colLaborID.setCellValueFactory(new PropertyValueFactory<>("laborID"));
        colLaborName.setCellValueFactory(new PropertyValueFactory<>("laborName"));
        colLaborAvailability.setCellValueFactory(new PropertyValueFactory<>("btn"));


    }

    private void loadAllLabors() {

        try {

            ObservableList<LaborTm> obList = FXCollections.observableArrayList();
            List<EmployeeDto> dtoList = employeeModel.loadAllLabors();

            for (int i = 0; i < dtoList.size(); i++) {



                JFXToggleButton btn = new JFXToggleButton();
                btn.setCursor(Cursor.HAND);


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


                var tm = new LaborTm(
                        dtoList.get(i).getEmpID(),
                        dtoList.get(i).getEmpName(),
                        btn
                );

                obList.add(tm);
            }
            tblLabor.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }



}
