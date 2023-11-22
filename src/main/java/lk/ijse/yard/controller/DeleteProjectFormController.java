package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.model.ProjectDetailsModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DeleteProjectFormController {

    private final ProjectDetailsModel projectDetailsModel = new ProjectDetailsModel();

    @FXML
    private JFXComboBox<String> cmbProjectNO;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXTextArea txtFieldLocation;

    @FXML
    private JFXTextArea txtFieldProjectName;

    @FXML
    void btnOnActionDELETEProject(ActionEvent event) {

        boolean isSelectProjectNO = isSelectProjectNO();

        if (isSelectProjectNO){

            String projectNO = cmbProjectNO.getValue();
            String projectName = txtFieldProjectName.getText();
            String projectLocation = txtFieldLocation.getText();
            LocalDate completionDate = datePicker.getValue();

            try {
                var projectDto = new ProjectDto(projectNO,projectName,projectLocation,completionDate);
                boolean isDeleted = projectDetailsModel.deleteProject(projectDto);
                if (isDeleted){
                    new Alert(Alert.AlertType.INFORMATION," Project is Deleted !!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private boolean isSelectProjectNO(){
        String projectNO = cmbProjectNO.getValue();
        if (projectNO == null){ new Alert(Alert.AlertType.ERROR,"Not Sellect Project NO !").show(); return false;}
        return true;
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setOldValuesforFields(ProjectDto dtoProject) {

        try {
            txtFieldProjectName.setText(dtoProject.getProjectName());
            txtFieldLocation.setText(dtoProject.getLoacation());
            datePicker.setValue(dtoProject.getCompletionDate());
        } catch (Exception e) {

        }

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


}
