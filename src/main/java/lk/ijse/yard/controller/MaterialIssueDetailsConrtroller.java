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
import lk.ijse.yard.dto.Tm.MaterialIssuedDetailsTm;
import lk.ijse.yard.dto.Tm.MaterialReceivedDetailsTm;
import lk.ijse.yard.model.*;

import java.io.IOException;
import java.security.Key;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MaterialIssueDetailsConrtroller {

    private final MaterialIssuedDetailsModel materialIssuedDetailsModel = new MaterialIssuedDetailsModel();
    private final MaterialModel materialModel = new MaterialModel();
    private final VehicleModel vehicleModel = new VehicleModel();
    private final ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();
    private final ProjectMaterialRequirementModel projectMaterialRequirementModel = new ProjectMaterialRequirementModel();
    private String  vID ;


    @FXML
    private JFXComboBox<String> cmbMaterialID;

    @FXML
    private JFXComboBox<String> cmbVehicleID;

    @FXML
    private JFXComboBox<String> cmbProjectNo;

    @FXML
    private TableColumn<?, ?> colActionDelete;

    @FXML
    private TableColumn<?, ?> colActionUpdate;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colIssuedID;

    @FXML
    private TableColumn<?, ?> colMaterialID;

    @FXML
    private TableColumn<?, ?> colMaterialName;

    @FXML
    private TableColumn<?, ?> colPrNO;

    @FXML
    private TableColumn<?, ?> colUnit;

    @FXML
    private TableColumn<?, ?> colVehicleID;

    @FXML
    private TableColumn<?, ?> colVehicleName;

    @FXML
    private TableColumn<?, ?> colmaterialType;

    @FXML
    private TableColumn<?, ?> colqty;

    @FXML
    private TableView<MaterialIssuedDetailsTm> tableIssueDetails;

    @FXML
    private DatePicker datePicker;

    @FXML
    private DatePicker datePickerUpdate;

    @FXML
    private Label lblMaterialName;

    @FXML
    private Label lblMaterialType;

    @FXML
    private Label lblUnit;

    @FXML
    private Label lblUnitUpdate;

    @FXML
    private Label lblStockQty;

    @FXML
    private Label lblVehicleName;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private JFXTextArea txtFieldMaterialIssuedID;

    @FXML
    private JFXTextArea txtMaterialQty;

    @FXML
    private JFXTextArea txtMaterialQtyUpdate;

    @FXML
    private AnchorPane updatePane;

    @FXML
    public void  initialize(){

        loadAllMaterialIssuedDetails();
        setCellValueFactory();

    }

    private void setCellValueFactory() {

        colDate.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        colPrNO.setCellValueFactory(new PropertyValueFactory<>("pNO"));
        colIssuedID.setCellValueFactory(new PropertyValueFactory<>("issuedID"));
        colMaterialID.setCellValueFactory(new PropertyValueFactory<>("mID"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colmaterialType.setCellValueFactory( new PropertyValueFactory<>("materialType"));
        colqty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        colVehicleName.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        colActionUpdate.setCellValueFactory(new PropertyValueFactory<>("btnUpdate"));
        colActionDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

    }

    private void loadAllMaterialIssuedDetails() {
        try {

            ObservableList<MaterialIssuedDetailsTm> obList = FXCollections.observableArrayList();

            List<MaterialIssuedDetailsDto> dtoList = materialIssuedDetailsModel.loadAllMIssuedDetails();

            for (int i = 0; i < dtoList.size(); i++) {

                MaterialDto materialDto = materialModel.findOldMaterialDetails(dtoList.get(i).getMaterialID());
                String vehicleName = vehicleModel.getVehicleName(dtoList.get(i).getVehicleID());
                Button btnUpdate = new Button("upd");
                Button btnDelete = new Button("del");
                btnUpdate.setCursor(Cursor.HAND); btnDelete.setCursor(Cursor.HAND);



                var dto = dtoList.get(i);
                btnDelete.setOnAction((e)->{

                    ButtonType yes = new ButtonType("yes",ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no",ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Please Confirm Delete Process !!",yes,no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tableIssueDetails.getSelectionModel().getSelectedIndex();
                        try {
                            boolean isDeleted = materialIssuedDetailsModel.deletIssuedDetails(dto,materialDto,vehicleName);

                            if (isDeleted){
                                /*obList.remove(selectedIndex);   //delete item from the JFX-Table
                                tableReceivedDetails.refresh();*/
                                loadAllMaterialIssuedDetails();
                                setCellValueFactory();
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });

                btnUpdate.setOnAction((e)->{
                    ButtonType yes = new ButtonType("yes",ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no",ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Please Confirm Update Process !!",yes,no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tableIssueDetails.getSelectionModel().getSelectedIndex();
                        tableIssueDetails.setDisable(true);
                        updatePane.setVisible(true);
                        updatePane.setManaged(true);



                    }

                });


                var tm = new MaterialIssuedDetailsTm (
                        dtoList.get(i).getIssuedDate(),
                        dtoList.get(i).getProjectNo(),
                        dtoList.get(i).getIssuedID(),
                        dtoList.get(i).getMaterialID(),
                        materialDto.getMaterialName(),
                        materialDto.getType(),
                        dtoList.get(i).getIssuedQty(),
                        materialDto.getUnit(),
                        dtoList.get(i).getVehicleID(),
                        vehicleName,
                        btnUpdate,
                        btnDelete
                );
                obList.add(tm);
            }
            tableIssueDetails.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddMAterialIssueDetails(ActionEvent event)  {
        try {
            boolean isValidIssueDetails = validateMaterialReceivedDetails();
            if (isValidIssueDetails){
                String materialIssuedID = txtFieldMaterialIssuedID.getText();
                String materialID = cmbMaterialID.getValue();
                String projectNo = cmbProjectNo.getValue();
                double issuedQty = Double.valueOf(txtMaterialQty.getText());

                LocalDate issuedDate  = datePicker.getValue();

                var issuedDto = new MaterialIssuedDetailsDto(
                        materialIssuedID,
                        materialID,
                        projectNo,
                        issuedQty,
                        vID,
                        issuedDate
                );

                boolean issuedSuccessed = materialIssuedDetailsModel.AddMaterialIssuedDetails(issuedDto);
                if (issuedSuccessed) {
                    new Alert(Alert.AlertType.INFORMATION,"Issued Successfully !").show();
                    loadAllMaterialIssuedDetails();
                    setCellValueFactory();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void clearFields() {
        txtFieldMaterialIssuedID.setText("");
        txtMaterialQty.setText("");
        cmbMaterialID.getSelectionModel().clearSelection();
        cmbProjectNo.getSelectionModel().clearSelection();
        cmbVehicleID.getSelectionModel().clearSelection();
        lblMaterialName.setText("");
        lblMaterialType.setText("");
        lblStockQty.setText("");
        lblVehicleName.setText("");
        lblUnit.setText("");
    }

    @FXML
    void cmbMaterialIDOnAction(ActionEvent event) {
        String materialId = cmbMaterialID.getValue();
        try {

            MaterialDto dto = materialModel.findOldMaterialDetails(materialId);
            double stockQty = materialModel.chekStock(materialId);
            lblStockQty.setText(String.valueOf(stockQty));

            if (dto != null){
                setOldDataforNewFields(dto);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void setOldDataforNewFields(MaterialDto dto) {
        setTextforIssueID(dto.getType());
        lblMaterialName.setText(dto.getMaterialName());
        lblMaterialType.setText(dto.getType());
        lblUnit.setText(dto.getUnit());
    }
    private void setTextforIssueID(String type) {
        if (type.equals("AGGREGATE")){ txtFieldMaterialIssuedID.setText("MI_AG"); }
        if (type.equals("SAND")){ txtFieldMaterialIssuedID.setText("MI_SA"); }
        if (type.equals("CEMENTS")){ txtFieldMaterialIssuedID.setText("MI_CE"); }
        if (type.equals("REINFORCEMENTS")){ txtFieldMaterialIssuedID.setText("MI_RE"); }
        if (type.equals("PLYWOOD")){ txtFieldMaterialIssuedID.setText("MI_PW"); }
    }

    @FXML
    void cmbMaterialIDonMousePressed(MouseEvent event) { loadAllMaterialsIDs(); }

    private void loadAllMaterialsIDs() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<MaterialDto> materialList = materialModel.loadAllMaterials();
            for (int i = 0; i < materialList.size(); i++) {

                obList.add(materialList.get(i).getMaterialID());

            }
            cmbMaterialID.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"Not add Materials in the System !!").show();
        }

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

    private boolean validateMaterialReceivedDetails() throws SQLException {

        String materialID = cmbMaterialID.getValue();
        if (materialID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Material ID").show(); return false ;}

        String receivedID = txtFieldMaterialIssuedID.getText();
        boolean isValidReceivedID = Pattern.matches("^MI_.*",receivedID);
        if (!isValidReceivedID){ new Alert(Alert.AlertType.ERROR,"Invalid Material Received ID").show(); return false; }

        LocalDate date = datePicker.getValue();
        if (date == null){ new Alert(Alert.AlertType.ERROR,"Not Select Issued Date").show(); return false ;}

        String pNo = cmbProjectNo.getValue();
        if (pNo == null){ new Alert(Alert.AlertType.ERROR,"Not Select Project").show(); return false ;}

        String vehicleID = cmbVehicleID.getValue();
        if (vehicleID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Vehicle ID").show(); return false ;}

        String qty = txtMaterialQty.getText();
        boolean isValidQty = Pattern.matches("^(?!0\\.0*$)\\d+(\\.\\d+)?$",qty);
        if (!isValidQty){ new Alert(Alert.AlertType.ERROR,"Invalid Material Qty").show(); return false; }

        double stockQty = materialModel.chekStock(materialID);
        if (stockQty < Double.valueOf(qty)){ new Alert(Alert.AlertType.ERROR,"Not enough qty on stock !!").show(); return false; }

        boolean nonRequirements = projectMaterialRequirementModel.checkNonRequiremets(pNo , materialID);
        if (nonRequirements)  { new Alert(Alert.AlertType.INFORMATION,materialID+"-"+lblMaterialName.getText()+" is Not Required ! . if You need issued this material for this Project Please update Project Requirements").show(); return false;}

        boolean requirementQtyExceed = projectMaterialRequirementModel.checkRequiremetsQtyExceed(pNo , materialID , Double.valueOf(qty));
        if (requirementQtyExceed){ new Alert(Alert.AlertType.INFORMATION,materialID+"-"+lblMaterialName.getText()+" Quantity Exceed ! . if You need issued this material for this Project Please update Project Requirements").show(); return false;}
        return  true;

    }

    @FXML
    void cmbProjectNOonMousePressed(MouseEvent event) { loadAllProjects();}

    private void loadAllProjects() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<ProjectDto> projectDtoList = projectDetailsModel.loadAllProjects();
            for (int i = 0; i < projectDtoList.size(); i++) {

                obList.add(projectDtoList.get(i).getProjectNO());

            }
            cmbProjectNo.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"Not add Projects in the System !!").show();
        }



    }

    @FXML
    void cmbProjectNoOnAction(ActionEvent event) { }

    @FXML
    void btnOnActionUpdateIssuedDetails(ActionEvent event) {

    }


    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/MaterialManage_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Material Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    @FXML
    void btnOnActionHidePane(ActionEvent event) {
        updatePane.setVisible(false);
        tableIssueDetails.setDisable(false);
    }


}


