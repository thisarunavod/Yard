package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.dto.MaterialReceivedDetailsDto;
import lk.ijse.yard.dto.Tm.MaterialReceivedDetailsTm;
import lk.ijse.yard.dto.Tm.VehicleRepairTm;
import lk.ijse.yard.dto.VehicleDto;
import lk.ijse.yard.dto.VehicleRepairDto;
import lk.ijse.yard.model.VehicleModel;
import lk.ijse.yard.model.VehicleRepairModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class VehicleRepairFormController {

    private final VehicleModel vehicleModel = new VehicleModel();
    private final VehicleRepairModel vehicleRepairModel = new VehicleRepairModel();

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private JFXComboBox<String> cmbVehicleID;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colRepID;

    @FXML
    private TableColumn<?, ?> colVehicleID;

    @FXML
    private TableColumn<?, ?> colVehicleName;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private DatePicker datePickerRepairdate;

    @FXML
    private Label lblVehicleName;

    @FXML
    private Label lblVehicleName1;

    @FXML
    private TableView<VehicleRepairTm> tableRepairDetails;

    @FXML
    private JFXTextArea txtFieldVehicleRapairCost;

    @FXML
    private JFXTextArea txtFieldVehicleRapairDescription;

    @FXML
    private JFXTextArea txtFieldVehicleRapairID;

    @FXML
    void initialize(){
        txtFieldVehicleRapairID.setText("REP_");
        loadAllRepairsDetails();
        setCellValueFactry();
    }


    @FXML
    void btnOnActionAddRepairDetailsDetails(ActionEvent event) throws SQLException {

        boolean isValidRepairDetails = validateRepairedDetails();
        if (isValidRepairDetails){

            try {
                String repairID = txtFieldVehicleRapairID.getText();
                String vID = cmbVehicleID.getValue();
                String description = txtFieldVehicleRapairDescription.getText();
                double cost =Double.valueOf(txtFieldVehicleRapairCost.getText());
                LocalDate date = datePickerRepairdate.getValue();

                var vehicleRepairDto = new VehicleRepairDto(
                        repairID,
                        vID,
                        description,
                        cost,
                        date
                );

                boolean isAdded = vehicleRepairModel.addRepairDetails(vehicleRepairDto);
                if (isAdded){
                    new Alert(Alert.AlertType.INFORMATION,"Repair details Added Successfully !!").show();
                    loadAllRepairsDetails();
                    setCellValueFactry();
                    clearFields();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

    }

    private void setCellValueFactry() {
        colRepID.setCellValueFactory(new PropertyValueFactory<>("repID"));
        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("repairDate"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
    }

    private void loadAllRepairsDetails() {

        try {
            ObservableList<VehicleRepairTm> obList = FXCollections.observableArrayList();

            List<VehicleRepairDto> dtoList = vehicleRepairModel.loadAllRepairDetails();
            for (int i = 0; i < dtoList.size(); i++) {

                Button btnDelete = new Button("del");
                String vehicleName = vehicleModel.getVehicleName(dtoList.get(i).getVehicleID());

                var tm = new VehicleRepairTm(
                        dtoList.get(i).getRepairID(),
                        dtoList.get(i).getVehicleID(),
                        vehicleName,
                        dtoList.get(i).getDescription(),
                        dtoList.get(i).getRepairCost(),
                        dtoList.get(i).getRepairDate(),
                        btnDelete
                );
                obList.add(tm);


                int finalI = i;
                btnDelete.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes",ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no",ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Please Confirm Delete Process",yes,no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tableRepairDetails.getSelectionModel().getSelectedIndex();
                        try {
                            boolean isDeleted = vehicleRepairModel.deletRepairDetails(dtoList.get(finalI).getRepairID());

                            if (isDeleted){
                                /*obList.remove(selectedIndex);   //delete item from the JFX-Table
                                tableReceivedDetails.refresh();*/
                                loadAllRepairsDetails();
                                setCellValueFactry();
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });







            }

            tableRepairDetails.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void clearFields() {
        txtFieldVehicleRapairID.setText("REP_");
        lblVehicleName.setText("");
        cmbVehicleID.getSelectionModel().clearSelection();
        txtFieldVehicleRapairDescription.setText("");
        txtFieldVehicleRapairCost.setText("");
    }

    @FXML
    void btnOnActionReportOfRepairDetailsDetails(ActionEvent event) { }

    @FXML
    void cmbVehicleIDOnAction(ActionEvent event) {

        String vehicleID = cmbVehicleID.getValue();

        try {
            String vehicleName = vehicleModel.getVehicleName(vehicleID);
            lblVehicleName.setText(vehicleName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void cmbVehicleIDonActionMousePressed(MouseEvent event) { loadAllVehicles(); }

    private void loadAllVehicles() {
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

    private boolean validateRepairedDetails() throws SQLException {


        String repairID = txtFieldVehicleRapairID.getText();
        boolean isValidReceivedID = Pattern.matches("^REP_.*",repairID);
        if (!isValidReceivedID){ new Alert(Alert.AlertType.ERROR,"Invalid Repair ID").show(); return false; }

        LocalDate date = datePickerRepairdate.getValue();
        if (date == null){ new Alert(Alert.AlertType.ERROR,"Not Select Repair Date").show(); return false ;}

        String vehicleID = cmbVehicleID.getValue();
        if (vehicleID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Vehicle ID !").show(); return false ;}

        String descriptionText = txtFieldVehicleRapairDescription.getText();
        boolean isValidText = Pattern.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+",descriptionText);
        if (!isValidText){ new Alert(Alert.AlertType.ERROR,"Invalid Description !").show(); return false; }

        String cost = txtFieldVehicleRapairCost.getText();
        boolean isValidCost = Pattern.matches("^(?!0\\.0*$)\\d+(\\.\\d+)?$",cost);
        if (!isValidCost){ new Alert(Alert.AlertType.ERROR,"Invalid Cost !").show(); return false; }

        return true;


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
