package lk.ijse.yard.controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.dto.Tm.MaterialTm;
import lk.ijse.yard.dto.Tm.ProjectMaterialRequirementTm;
import lk.ijse.yard.model.MaterialModel;
import lk.ijse.yard.model.ProjectDetailsModel;
import lk.ijse.yard.model.ProjectMaterialRequirementModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class AddProjectFormController {
    private final MaterialModel materialModel = new MaterialModel();
    private final ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();
    private final ProjectMaterialRequirementModel requirementModel = new ProjectMaterialRequirementModel();

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private TableColumn<?, ?> colMID;

    @FXML
    private TableColumn<?, ?> colMName;

    @FXML
    private TableColumn<?, ?> colRQty;

    @FXML
    private TableColumn<?, ?> colUnit;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<ProjectMaterialRequirementTm> tableMaterialRequiremets;

    @FXML
    private JFXTextArea txtFieldLocation;

    @FXML
    private JFXTextArea txtFieldProjectName;

    @FXML
    private JFXTextArea txtFieldProjectNo;

    ObservableList<ProjectMaterialRequirementTm> obList;

    @FXML
    void initialize(){

        loadAllMaterials();
        setCellValueFactory();

    }

    private void setCellValueFactory() {
        colMID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colMName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colRQty.setCellValueFactory(new PropertyValueFactory<>("reqQty"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    private void loadAllMaterials() {

        try {
            List<MaterialDto> dtoList = materialModel.loadAllMaterials();
            obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {


                TextField reqQty = new TextField();
                reqQty.setText("0");

                var tm = new ProjectMaterialRequirementTm(
                        dtoList.get(i).getMaterialID(),
                        dtoList.get(i).getMaterialName(),
                        reqQty,
                        dtoList.get(i).getUnit()
                );
                obList.add(tm);
            }
            tableMaterialRequiremets.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnOnActionAddProject(ActionEvent event) {

        boolean projectDetailsValidation = validProjectDetails();
        boolean reqQtyValidation = validReqQty();

        if (projectDetailsValidation && reqQtyValidation ){

            String projectNO = txtFieldProjectNo.getText();
            String projectName = txtFieldProjectName.getText();
            String projectLocation = txtFieldLocation.getText();
            LocalDate completionDate = datePicker.getValue();

            var projectDto = new ProjectDto(projectNO,projectName,projectLocation,completionDate);

            try {

                boolean isAddprojectDetail = projectDetailsModel.AddProjectDetails(projectDto , obList);
                if (isAddprojectDetail){
                    new Alert(Alert.AlertType.INFORMATION,projectDto.getProjectNO()+" - "+projectDto.getProjectName() + " is Added Successfully !!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validReqQty() {
        for (int i = 0; i <obList.size() ; i++) {

            String qty = obList.get(i).getReqQty().getText();
            boolean validQty = Pattern.matches("^(?:0|[1-9]\\d*)(?:\\.\\d+)?$",qty);
            if (!validQty){ new Alert(Alert.AlertType.ERROR,"invalid Qty include").show(); return false;}

        }
        return true;
    }

    private boolean validProjectDetails() {

        String projectNO = txtFieldProjectNo.getText();
        boolean validProjectNo = Pattern.matches("^P\\d+$",projectNO);
        if (!validProjectNo){ new Alert(Alert.AlertType.ERROR,"invalid Project NO !").show(); return false;}

        String projectName = txtFieldProjectName.getText();
        boolean validName = Pattern.matches("^[A-Za-z'\\s-]+$",projectName);
        if (!validName){ new Alert(Alert.AlertType.ERROR,"invalid Project Name !").show(); return false;}

        String location = txtFieldLocation.getText();
        boolean validLocation = Pattern.matches("^[A-Za-z'\\s-]+$",location);
        if (!validLocation){ new Alert(Alert.AlertType.ERROR,"invalid Project Location !").show(); return false;}

        LocalDate date = datePicker.getValue();
        if (date == null){ new Alert(Alert.AlertType.ERROR,"Not Select Completion Date").show(); return false ;}

        return true;
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/projects_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Dash board");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

}
