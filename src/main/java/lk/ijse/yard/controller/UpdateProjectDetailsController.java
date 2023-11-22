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
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.dto.Tm.ProjectMaterialRequirementTm;
import lk.ijse.yard.model.MaterialModel;
import lk.ijse.yard.model.ProjectDetailsModel;
import lk.ijse.yard.model.ProjectMaterialRequirementModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class UpdateProjectDetailsController {

    private final ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();
    private final MaterialModel materialModel = new MaterialModel();
    ObservableList<ProjectMaterialRequirementTm> obList;

    @FXML
    private TableColumn<?, ?> colMID;

    @FXML
    private TableColumn<?, ?> colMName;

    @FXML
    private TableColumn<?, ?> colRQty;

    @FXML
    private TableColumn<?, ?> colUnit;

    @FXML
    private TableView<ProjectMaterialRequirementTm> tableMaterialRequiremets;

    @FXML
    private JFXComboBox<String> cmbProjectNO;

    @FXML
    private DatePicker datePicker;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private JFXTextArea txtFieldLocation;

    @FXML
    private JFXTextArea txtFieldProjectName;


    @FXML
    void btnOnActionUPDATEProject(ActionEvent event)  {
        boolean newProjectDetailsValidation = validNewProjectDetails();
        boolean newMaterialRequirementValidation = validReqQty();

        String projectNO = cmbProjectNO.getValue();
        String projectName = txtFieldProjectName.getText();
        String projectLocation = txtFieldLocation.getText();
        LocalDate completionDate = datePicker.getValue();
        try {
            if (newProjectDetailsValidation && newMaterialRequirementValidation ){
                var projectDto = new ProjectDto(projectNO,projectName,projectLocation,completionDate);

                boolean updateProjectAllDetails = projectDetailsModel.updateAllProjectDetails(projectDto,obList);
                if (updateProjectAllDetails){
                    new Alert(Alert.AlertType.INFORMATION," Successfully Updated !!").show();
                }

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void cmbIDonMousePressed(MouseEvent event) {
        loadAllProjectNO();
    }

    @FXML
    void cmbProjectNOOnAction(ActionEvent event) {
        String projectNO = cmbProjectNO.getValue();
        try {
            ProjectDto dtoProject = projectDetailsModel.findOldProjectDetails(projectNO);
            setOldValuesforFields(dtoProject);
            loadAllReqMaterials(dtoProject);
            setCellValueFactory();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setOldValuesforFields(ProjectDto dto) {
        try {
            txtFieldProjectName.setText(dto.getProjectName());
            txtFieldLocation.setText(dto.getLoacation());
            datePicker.setValue(dto.getCompletionDate());
        } catch (Exception e) {

        }
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

    private void loadAllProjectNO() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<ProjectDto> projectDtoList = projectDetailsModel.loadAllProjects();
            for (int i = 0; i < projectDtoList.size(); i++) {

                obList.add(projectDtoList.get(i).getProjectNO());

            }
            cmbProjectNO.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"Not add Projects in the System !!").show();
        }

    }

    private void loadAllReqMaterials(ProjectDto dto) {

        try {
            List<MaterialDto> dtoList = materialModel.loadAllMaterials();
            obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {

                TextField reqQty = new TextField();
                String Qty = ProjectMaterialRequirementModel.getRequiredQty(dto.getProjectNO(),dtoList.get(i).getMaterialID());
                reqQty.setText(Qty);

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

    private void setCellValueFactory() {
        colMID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colMName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colRQty.setCellValueFactory(new PropertyValueFactory<>("reqQty"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    private boolean validReqQty() {
        for (int i = 0; i < obList.size() ; i++) {

            String qty = obList.get(i).getReqQty().getText();
            boolean validQty = Pattern.matches("^(?:0|[1-9]\\d*)(?:\\.\\d+)?$",qty);
            if (!validQty){ new Alert(Alert.AlertType.ERROR,"invalid Qty include").show(); return false;}

        }
        return true;
    }

    private boolean validNewProjectDetails() {

        String projectNO = cmbProjectNO.getValue();
        if (projectNO == null){ new Alert(Alert.AlertType.ERROR,"Not Sellect Project NO !").show(); return false;}

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



}
