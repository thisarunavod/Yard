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
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.MaterialReceivedDetailsDto;
import lk.ijse.yard.dto.SupplierDto;
import lk.ijse.yard.dto.Tm.MaterialReceivedDetailsTm;
import lk.ijse.yard.model.MaterialModel;
import lk.ijse.yard.model.MaterialReceivedDetailsModel;
import lk.ijse.yard.model.SupplierModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MaterialReceivedDetailsController {

    private final MaterialModel materialModel = new MaterialModel();
    private final SupplierModel supplierModel = new SupplierModel();
    private final MaterialReceivedDetailsModel materialReceivedDetailsModel = new MaterialReceivedDetailsModel();

    @FXML
    private AnchorPane mainRoot;
    @FXML
    private JFXComboBox<String> cmbMaterialID;

    @FXML
    private JFXComboBox<String> cmbMaterialType;

    @FXML
    private JFXComboBox<String> cmbSupplierID;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblUnit;

    @FXML
    private JFXTextArea txtFieldMaterialReceivedID;

    @FXML
    private JFXTextArea txtMaterialName;

    @FXML
    private JFXTextArea txtMaterialQty;
    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colMaterialID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colReceivedID;

    @FXML
    private TableColumn<?, ?> colSupID;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnit;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colActionUpdate;

    @FXML
    private TableView<MaterialReceivedDetailsTm> tableReceivedDetails;


    @FXML
    void initialize(){
        loadAllReceivedDetails();
        setCellValueFactory();

    }
    private void setCellValueFactory() {

        colMaterialID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colReceivedID.setCellValueFactory(new PropertyValueFactory<>("receivedId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
        colSupID.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("materialType"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("receivedQty"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnUpdate"));
        colActionUpdate.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

    }

    private void loadAllReceivedDetails() {
        try {

            ObservableList<MaterialReceivedDetailsTm> obList = FXCollections.observableArrayList();

            List<MaterialReceivedDetailsDto> dtoList = materialReceivedDetailsModel.loadAllMReceivedDetails();

            for (int i = 0; i < dtoList.size(); i++) {

                Button btnUpdate = new Button("upd");
                Button btnDelete = new Button("del");
                btnUpdate.setCursor(Cursor.HAND); btnDelete.setCursor(Cursor.HAND);
                var tm = new MaterialReceivedDetailsTm(
                        dtoList.get(i).getMaterialID(),
                        dtoList.get(i).getReceivedId(),
                        dtoList.get(i).getReceivedDate(),
                        dtoList.get(i).getSupId(),
                        dtoList.get(i).getMaterialName(),
                        dtoList.get(i).getMaterialType(),
                        dtoList.get(i).getReceivedQty(),
                        dtoList.get(i).getUnit(),
                        btnUpdate,
                        btnDelete
                );
                obList.add(tm);


                var dto = dtoList.get(i);
                btnDelete.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes",ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no",ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Please Confirm Delete Process",yes,no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tableReceivedDetails.getSelectionModel().getSelectedIndex();
                        try {
                            boolean isDeleted = materialReceivedDetailsModel.deletReceivedDetails(dto);

                            if (isDeleted){
                                /*obList.remove(selectedIndex);   //delete item from the JFX-Table
                                tableReceivedDetails.refresh();*/
                                loadAllReceivedDetails();
                                setCellValueFactory();
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                });

                btnUpdate.setOnAction((e) -> {


                });

            }
            tableReceivedDetails.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cmbIDonMousePressed(MouseEvent event) { laodAllMaterialsID(); }

    @FXML
    void cmbSupplierIDOnMosePressed(MouseEvent event) { loadAllSupplierIDs();}

    @FXML
    void cmbMaterialIDOnAction(ActionEvent event) {
        String materialId = cmbMaterialID.getValue();
        try {

            MaterialDto dto = materialModel.findOldMaterialDetails(materialId);
            if (dto != null){
                setOldDataforNewFields(dto);
            }else{
//                new Alert(Alert.AlertType.INFORMATION,"Material Not Found").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }


    }

    @FXML
    void btnAddMAterialReceivedDetails(ActionEvent event) {

        boolean isValid = validateMaterialReceivedDetails();

        String materialID = cmbMaterialID.getValue();
        String materialReceivedID = txtFieldMaterialReceivedID.getText();
        LocalDate date = datePicker.getValue();
        String supplierID = cmbSupplierID.getValue();
        String materialName = txtMaterialName.getText();
        String materialType = cmbMaterialType.getValue();
        double qty = -1;
        try {
             qty = Double.parseDouble(txtMaterialQty.getText());
        } catch (NumberFormatException e) {

        }
        String unit = lblUnit.getText();

        if (isValid){

            var receivedDetailsDto = new MaterialReceivedDetailsDto(
                    materialID,
                    materialReceivedID,
                    date,
                    supplierID,
                    materialName,
                    materialType,
                    qty,
                    unit
            );

            try {
                boolean isSaved = materialReceivedDetailsModel.addMaterialReceivedDetails(receivedDetailsDto);
                if (isSaved){
                    loadAllReceivedDetails();
                    setCellValueFactory();
                    new Alert(Alert.AlertType.INFORMATION,"Material Received Details Added Successfully !! ").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/MaterialManage_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Dash board");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnAddNewMaterialID(ActionEvent event) throws IOException { goToMaterialManagePage(); }

    @FXML
    void btnNewSupplierID(ActionEvent event) throws IOException { goToSupplierManagePage(); }


    private void setOldDataforNewFields(MaterialDto dto) {
        setTextforReceivedID(dto.getType());
        txtMaterialName.setText(dto.getMaterialName());
        setOldTypeforCmbBox(dto);
        lblUnit.setText(dto.getUnit());
    }
    private void setTextforReceivedID(String type) {
        if (type.equals("AGGREGATE")){ txtFieldMaterialReceivedID.setText("MR_AG"); }
        if (type.equals("SAND")){ txtFieldMaterialReceivedID.setText("MR_SA"); }
        if (type.equals("CEMENTS")){ txtFieldMaterialReceivedID.setText("MR_CE"); }
        if (type.equals("REINFORCEMENTS")){ txtFieldMaterialReceivedID.setText("MR_RE"); }
        if (type.equals("PLYWOOD")){ txtFieldMaterialReceivedID.setText("MR_PW"); }
    }
    private void setOldTypeforCmbBox(MaterialDto dto) {
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("AGGREGATE");
        type.add("SAND");
        type.add("CEMENTS");
        type.add("REINFORCEMENTS");
        type.add("PLYWOOD");
        cmbMaterialType.setItems(type);
        cmbMaterialType.setValue(dto.getType());
    }

    private void laodAllMaterialsID() {

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
    private void loadAllSupplierIDs() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<SupplierDto> supplierDtoList = supplierModel.loadAllSuppliers();
            for (int i = 0; i < supplierDtoList.size(); i++) {
                obList.add(supplierDtoList.get(i).getSupplierId());
            }
            cmbSupplierID.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"Not add Suppliers in the System !!").show();
        }

    }
    private void goToSupplierManagePage() throws IOException {
        Stage stage = new Stage();
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/addSupplier_form.fxml"));
        Scene scene = new Scene(rootNode);
        stage.setTitle("Add Supplier");
        stage.centerOnScreen();
        stage.setScene(scene);

        stage.show();
    }
    private void goToMaterialManagePage() throws IOException {
        Stage stage = new Stage();
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/addMaterial_form.fxml"));
        Scene scene = new Scene(rootNode);
        stage.setTitle("Add Material");
        stage.centerOnScreen();
        stage.setScene(scene);

        stage.show();
    }

    private boolean validateMaterialReceivedDetails() {
        String materialID = cmbMaterialID.getValue();
        if (materialID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Material ID").show(); return false ;}

        String receivedID = txtFieldMaterialReceivedID.getText();
        boolean isValidReceivedID = Pattern.matches("^MR_.*",receivedID);
        if (!isValidReceivedID){ new Alert(Alert.AlertType.ERROR,"Invalid Material Received ID").show(); return false; }


        LocalDate date = datePicker.getValue();
        if (date == null){ new Alert(Alert.AlertType.ERROR,"Not Select received Date").show(); return false ;}


        String supplierID = cmbSupplierID.getValue();
        if (supplierID == null){ new Alert(Alert.AlertType.ERROR,"Not Select Supplier ID").show(); return false ;}

        String name = txtMaterialName.getText();
        boolean isValidName =validText(name);
        if (!isValidName){ new Alert(Alert.AlertType.ERROR,"Invalid Material Name").show(); return false; }

//        ^[1-9]\d*(\.\d+)?$
        String qty = txtMaterialQty.getText();
        boolean isValidQty = Pattern.matches("^[1-9]\\d*(\\.\\d+)?$",qty);
        if (!isValidQty){ new Alert(Alert.AlertType.ERROR,"Invalid Material Qty").show(); return false; }

        return  true;
    }
    private boolean validText(String text){
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ' ){  return true;}
        }
        return false;
    }

}
