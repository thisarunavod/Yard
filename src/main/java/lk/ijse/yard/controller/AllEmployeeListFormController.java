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
import lk.ijse.yard.dto.Tm.EmployeeListTm;
import lk.ijse.yard.dto.Tm.LaborTm;
import lk.ijse.yard.model.EmployeeModel;

import java.sql.SQLException;
import java.util.List;

public class AllEmployeeListFormController {
    private final EmployeeModel employeeModel = new EmployeeModel();
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colAvailability;

    @FXML
    private TableColumn<?, ?> colEmployeeID;

    @FXML
    private TableColumn<?, ?> colEmployeeName;

    @FXML
    private TableColumn<?, ?> colJobType;

    @FXML
    private TableView<EmployeeListTm> tblLabor;

    @FXML
    void initialize(){
        loadAllEmployees();
        setCellValueFactory();
    }
    private void setCellValueFactory() {

        colEmployeeID.setCellValueFactory(new PropertyValueFactory<>("empID"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("empAddress"));
        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));

    }

    private void loadAllEmployees() {

        try {

            ObservableList<EmployeeListTm> obList = FXCollections.observableArrayList();
            List<EmployeeDto> dtoList = employeeModel.loadAllEmployees();

            for (int i = 0; i < dtoList.size(); i++) {

                var tm = new EmployeeListTm(
                        dtoList.get(i).getEmpID(),
                        dtoList.get(i).getEmpName(),
                        dtoList.get(i).getEmpAddress(),
                        dtoList.get(i).getJobTitle(),
                        dtoList.get(i).getAvailability()
                );

                obList.add(tm);
            }
            tblLabor.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

}
