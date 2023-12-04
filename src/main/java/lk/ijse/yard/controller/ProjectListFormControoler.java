package lk.ijse.yard.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.dto.ProjectMaterialRequirementDto;
import lk.ijse.yard.dto.Tm.MaterialTm;
import lk.ijse.yard.dto.Tm.ProjectListTm;
import lk.ijse.yard.model.ProjectDetailsModel;
import lk.ijse.yard.model.ProjectMaterialRequirementModel;

import java.sql.SQLException;
import java.util.List;

public class ProjectListFormControoler {

    private ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();
    private ProjectMaterialRequirementModel projectMaterialRequirementModel = new ProjectMaterialRequirementModel();
    @FXML
    private TableColumn<?, ?> colCompletionDate;

    @FXML
    private TableColumn<?, ?> colCompletionProgress;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrecentage;

    @FXML
    private TableColumn<?, ?> colProjectNO;

    @FXML
    private TableView<ProjectListTm> tableProjectList;

    @FXML
    void initialize(){
        loadAllProjects();
        setCellValueFactory();
    }

    private void setCellValueFactory() {

        colProjectNO.setCellValueFactory(new PropertyValueFactory<>("projectNO"));
        colName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colCompletionDate.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        colCompletionProgress.setCellValueFactory(new PropertyValueFactory<>("progressCompletion"));
        colPrecentage.setCellValueFactory(new PropertyValueFactory<>("precentage"));

    }

    private void loadAllProjects() {

        try {
            List<ProjectDto> dtoList = projectDetailsModel.loadAllProjects();
            ObservableList<ProjectListTm> obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {


                ProgressBar progress = new ProgressBar();
                progress.setPrefWidth(180);
                List<ProjectMaterialRequirementDto> reqList = projectMaterialRequirementModel.loadRequirements(dtoList.get(i).getProjectNO());
                double precentage = 0;
                double totalReq = 0;
                for (int j = 0; j < reqList.size(); j++) { totalReq += reqList.get(j).getReqQty(); }

                for (int j = 0; j < reqList.size(); j++) {
                    precentage += (reqList.get(j).getReqQty()/totalReq)*(reqList.get(j).getIssuedQty()/reqList.get(j).getReqQty());
                    if (i == reqList.size()- 1){totalReq = 0;}
                }
                progress.setProgress(precentage);

                var tm = new ProjectListTm(
                        dtoList.get(i).getProjectNO(),
                        dtoList.get(i).getProjectName(),
                        dtoList.get(i).getCompletionDate(),
                        progress,
                        String.format("%.2f", (precentage*100))+" %"
                );

                obList.add(tm);
            }
            tableProjectList.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

}
