package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.yard.dto.MaterialIssuedDetailsDto;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.dto.ProjectMaterialRequirementDto;
import lk.ijse.yard.dto.Tm.MaterialIssuedDetailsTm;
import lk.ijse.yard.dto.Tm.ViewMaterialRequirementsTm;
import lk.ijse.yard.model.MaterialModel;
import lk.ijse.yard.model.ProjectDetailsModel;
import lk.ijse.yard.model.ProjectMaterialRequirementModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewProjectDetailsController {

    private final ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();
    private final ProjectMaterialRequirementModel projectMaterialRequirementModel = new ProjectMaterialRequirementModel();
    private final MaterialModel materialModel = new MaterialModel();

    @FXML
    private JFXComboBox<String> cmbProjectNo;

    @FXML
    private TableColumn<?, ?> colIssuedQty;

    @FXML
    private TableColumn<?, ?> colMaterialID;

    @FXML
    private TableColumn<?, ?> colMaterialName;

    @FXML
    private TableColumn<?, ?> colMoreReqQty;

    @FXML
    private TableColumn<?, ?> colReqQty;

    @FXML
    private TableColumn<?, ?> colSuppliments;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnit;

    @FXML
    private TableView<ViewMaterialRequirementsTm> tableViewMaterialReqAndSuppliments;

    @FXML
    private Label lblCompletionDate;

    @FXML
    private Label lblProjectLocation;

    @FXML
    private Label lblProjectName;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    public void initialize(){

    }


    @FXML
    void cmbOnActionSelectProjectNO(ActionEvent event) {

        String pNO = cmbProjectNo.getValue();

        try {
            ProjectDto dto = projectDetailsModel.findOldProjectDetails(pNO);
            if (dto != null){
                lblProjectName.setText(dto.getProjectName());
                lblProjectLocation.setText(dto.getLoacation());
                lblCompletionDate.setText(dto.getCompletionDate().toString());

                loadMaterialRequirements(dto);
                setCellValueFactry();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactry() {
        colMaterialID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("materialType"));
        colIssuedQty.setCellValueFactory(new PropertyValueFactory<>("issuedQty"));
        colReqQty.setCellValueFactory(new PropertyValueFactory<>("reqQty"));
        colMoreReqQty.setCellValueFactory(new PropertyValueFactory<>("moreReqQty"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colSuppliments.setCellValueFactory(new PropertyValueFactory<>("suppliments"));

    }

    private void loadMaterialRequirements(ProjectDto dto) {

        try {
            ObservableList<ViewMaterialRequirementsTm> obList = FXCollections.observableArrayList();

            List<ProjectMaterialRequirementDto> dtoList = projectMaterialRequirementModel.loadRequirements(dto.getProjectNO());

            for (int i = 0; i < dtoList.size(); i++) {


                MaterialDto materialDto  = materialModel.findOldMaterialDetails(dtoList.get(i).getMaterialID());

                ProgressBar suppliments = new ProgressBar();
                suppliments.setPrefWidth(250);
                double supplimentsRatio = dtoList.get(i).getIssuedQty() / dtoList.get(i).getReqQty();
//                if (supplimentsRatio > 0.9){ }
                suppliments.setProgress(supplimentsRatio);

                var tm = new ViewMaterialRequirementsTm(
                        dtoList.get(i).getMaterialID(),
                        materialDto.getMaterialName(),
                        materialDto.getType(),
                        dtoList.get(i).getIssuedQty(),
                        dtoList.get(i).getReqQty(),
                        (dtoList.get(i).getReqQty() - dtoList.get(i).getIssuedQty()),
                        dtoList.get(i).getUnit(),
                        suppliments
                );

                obList.add(tm);
            }

            tableViewMaterialReqAndSuppliments.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void cmbProjectNOMousePressed(MouseEvent event) { loadAllProjects(); }

    private void loadAllProjects() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<ProjectDto> projectDtoList = projectDetailsModel.loadAllProjects();

            for (int i = 0; i < projectDtoList.size(); i++) {

                obList.add(projectDtoList.get(i).getProjectNO());

            }
            cmbProjectNo.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"Not Added Projects in the System !!").show();
        }

    }

    @FXML
    void tableMaterialRequirementsAndSuppliments(ActionEvent event) {

    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/projects_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Projects Form");
        stage.setScene(scene);
        stage.centerOnScreen();

    }


}
