package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.dto.*;
import lk.ijse.yard.dto.Tm.MachineIssueDetailsTm;
import lk.ijse.yard.dto.Tm.MaterialReceivedDetailsTm;
import lk.ijse.yard.model.MachineIssuedDetailsModel;
import lk.ijse.yard.model.MachineModel;
import lk.ijse.yard.model.ProjectDetailsModel;
import lk.ijse.yard.model.VehicleModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MachineIssueDetailsFormController {
    private MachineModel machineModel = new MachineModel();
    private VehicleModel vehicleModel = new VehicleModel();
    private ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();
    private MachineIssuedDetailsModel machineIssuedDetailsModel = new MachineIssuedDetailsModel();
    private String  vID ;
    private String  mID ;
    private String  pNO;

    @FXML
    private JFXComboBox<String> cmbMachineID;

    @FXML
    private JFXComboBox<String> cmbProjectNo;

    @FXML
    private JFXComboBox<String> cmbVehicleID;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colIssuedIUD;

    @FXML
    private TableColumn<?, ?> colMachineID;

    @FXML
    private TableColumn<?, ?> colMachineName;

    @FXML
    private TableColumn<?, ?> colProjectNO;

    @FXML
    private TableColumn<?, ?> colVehicleID;

    @FXML
    private TableColumn<?, ?> colVehicleName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblMachineName;

    @FXML
    private Label lblVehicleName;

    @FXML
    private TableColumn<?, ?> colIssuedDate;

    @FXML
    private TableView<MachineIssueDetailsTm> tableMachineIssueDetails;

    @FXML
    private JFXTextArea txtFieldMachineIssuedID;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    void initialize(){
        txtFieldMachineIssuedID.setText("MAI_");
        loadAllIssuedDetails();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colIssuedDate.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        colProjectNO.setCellValueFactory(new PropertyValueFactory<>("projectNO"));
        colIssuedIUD.setCellValueFactory(new PropertyValueFactory<>("machineIssuedID"));
        colMachineID.setCellValueFactory(new PropertyValueFactory<>("machineID"));
        colMachineName.setCellValueFactory(new PropertyValueFactory<>("machineName"));
        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("del"));

    }

    private void loadAllIssuedDetails() {
        try {

            ObservableList<MachineIssueDetailsTm> obList = FXCollections.observableArrayList();

            List<MachineIssuedDetailsDto> dtoList = machineIssuedDetailsModel.loadAllMachineIssuedDetails();

            for (int i = 0; i < dtoList.size(); i++) {

                Button btnDelete = new Button("del");
                btnDelete.setCursor(Cursor.HAND);
                String machineName = machineModel.getMachineName(dtoList.get(i).getMachineID());
                String vehicleName = vehicleModel.getVehicleName(dtoList.get(i).getVehicleID());
                var tm = new MachineIssueDetailsTm(
                        dtoList.get(i).getIssueDate(),
                        dtoList.get(i).getProjectNO(),
                        dtoList.get(i).getMachineIssuedID(),
                        dtoList.get(i).getMachineID(),
                        machineName,
                        dtoList.get(i).getVehicleID(),
                        vehicleName,
                        btnDelete
                );
                obList.add(tm);


                var dto = dtoList.get(i);
                btnDelete.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes",ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no",ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Please Confirm Delete Process",yes,no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tableMachineIssueDetails.getSelectionModel().getSelectedIndex();
                        try {
                            boolean isDeleted = machineIssuedDetailsModel.deletReceivedDetails(dto);

                            if (isDeleted){
                                /*obList.remove(selectedIndex);   //delete item from the JFX-Table
                                tableMachineIssueDetails.refresh();
                                */
                                loadAllIssuedDetails();
                                setCellValueFactory();
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });

            }
            tableMachineIssueDetails.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddMachineIssueDetails(ActionEvent event) {

        boolean isvalidMachineIssuedDetails = validateMachineIssuedDetails();
        if (isvalidMachineIssuedDetails){
            try {
                String machineID = mID;
                String issuedID = txtFieldMachineIssuedID.getText();
                String projectNO = pNO;
                String vehicleID = vID;
                LocalDate issuedDate = datePicker.getValue();
                String machineName = lblMachineName.getText();
                String vehicleName = lblVehicleName.getText();

                var dto = new MachineIssuedDetailsDto(
                        issuedID,machineID,projectNO,vehicleID,issuedDate
                );
                boolean isAddMachineIssuedDetails = machineIssuedDetailsModel.addIssueDetails(dto);
                if (isAddMachineIssuedDetails){
                    new Alert(Alert.AlertType.INFORMATION,dto.getMachineID()+" is Issued Successfully !!").show();
                    loadAllIssuedDetails();
                    setCellValueFactory();
                    clearFields();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        txtFieldMachineIssuedID.setText("MAI_");
        cmbMachineID.getSelectionModel().clearSelection();
        cmbProjectNo.getSelectionModel().clearSelection();
        cmbVehicleID.getSelectionModel().clearSelection();
        lblMachineName.setText("");
        lblVehicleName.setText("");
    }

    @FXML
    void cmbMachineIDOnAction(ActionEvent event) {
        mID = "";
        try {
            if (cmbMachineID.getValue() != null){
                for (int i = 0; i < cmbMachineID.getValue().length(); i++) {
                    mID += cmbMachineID.getValue().charAt(i);
                    if (i == 4) { break;}
                }
            }

            lblMachineName.setText( machineModel.getMachineName(mID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void cmbMachineIDonMousePressed(MouseEvent event) { loadAvailableMachines(); }

    private void loadAvailableMachines(){

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<MachineDto> machineDtoList = machineModel.loadAllAvailableMachines();
            for (int i = 0; i < machineDtoList.size(); i++) {
                obList.add(machineDtoList.get(i).getMachineID()+"-"+machineDtoList.get(i).getMachineName());
            }
            cmbMachineID.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void cmbProjectNOonMousePressed(MouseEvent event) { loadAllProjects(); }
    private void loadAllProjects(){

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<ProjectDto> projectDtoList = projectDetailsModel.loadAllProjects();
            for (int i = 0; i < projectDtoList.size(); i++) {
                obList.add(projectDtoList.get(i).getProjectNO()+"-"+projectDtoList.get(i).getProjectName());
            }
            cmbProjectNo.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void cmbProjectNoOnAction(ActionEvent event) {
        pNO = "";
        if (cmbProjectNo.getValue() != null){
            for (int i = 0; i < cmbProjectNo.getValue().length(); i++) {
                pNO += cmbProjectNo.getValue().charAt(i);
                if (i == 3) { break;}
            }
        }
        System.out.println(pNO);
    }


    @FXML
    void cmbVehicleIDOnAction(ActionEvent event) {
        vID = "";
        try {
            if (cmbVehicleID.getValue() != null){
                for (int i = 0; i < cmbVehicleID.getValue().length(); i++) {
                    vID += cmbVehicleID.getValue().charAt(i);
                    if (i == 3) { break;}
                }
            }
            lblVehicleName.setText( vehicleModel.getVehicleName(vID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void cmbVehicleIDonMousePressed(MouseEvent event) { loadAllAvailableVehicles(); }

    private void loadAllAvailableVehicles() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<VehicleDto> vehicleList = vehicleModel.loadAllAvailbleVehiclesForUse();
            if (vehicleList.get(0).getRootStatus().equals("ON_yard")){
                for (int i = 0; i < vehicleList.size(); i++) {
                    obList.add(vehicleList.get(i).getVehicleId() + "-"+vehicleList.get(i).getVehicleName());
                }
                cmbVehicleID.setItems(obList);
            }else{ new Alert(Alert.AlertType.INFORMATION,"Not Available Vehicle On this time !!").show();}

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }


    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/machines/machineManage_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Machine Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    private boolean validateMachineIssuedDetails() {

        String machineID = cmbMachineID.getValue();
        if (machineID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Machine ID").show(); return false ;}

        String IssuedID = txtFieldMachineIssuedID.getText();
        boolean isValidIssuedID = Pattern.matches("^MAI_.*",IssuedID);
        if (!isValidIssuedID){ new Alert(Alert.AlertType.ERROR,"Invalid Machine Issued ID").show(); return false; }

        LocalDate IssuedDate = datePicker.getValue();
        if (IssuedDate == null){ new Alert(Alert.AlertType.ERROR,"Not Select Issued Date").show(); return false ;}

        String pNo = cmbProjectNo.getValue();
        if (pNo == null){ new Alert(Alert.AlertType.ERROR,"Not Select Project").show(); return false ;}

        String vehicleID = cmbVehicleID.getValue();
        if (vehicleID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Vehicle ID").show(); return false ;}

        return true;

    }


}
