package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectsFormController {
    @FXML
    private JFXButton btnADDproject;

    @FXML
    private JFXButton btnDeleteProject;

    @FXML
    private JFXButton btnProjectList;

    @FXML
    private JFXButton btnUpdateProject;

    @FXML
    private AnchorPane mainRoot;

    @FXML
    private AnchorPane root;

    @FXML
    void btnAddProjectOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/addProject_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Received Details");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/dash_Board.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Dash board");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnDeleteProjectOnAction(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/deleteProject_form.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(rootNode);

    }

    @FXML
    void btnOnActionProjectList(ActionEvent event) throws IOException {


    }

    @FXML
    void OnActionViewProjectDetails(ActionEvent event) throws IOException {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/viewProjectDetails.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("View Project Details");
        stage.setScene(scene);
        stage.centerOnScreen();

    }

    @FXML
    void btnOnActionUpdateProject(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/updateProjectDetails.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainRoot.getScene().getWindow();
        stage.setTitle("Update Project");
        stage.setScene(scene);
        stage.centerOnScreen();
    }

}


