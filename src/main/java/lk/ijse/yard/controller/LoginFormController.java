package lk.ijse.yard.controller;


import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.yard.dto.UserDto;
import lk.ijse.yard.model.LoginModel;

import javax.sound.midi.MidiFileFormat;
import java.io.IOException;
import java.sql.SQLException;

import static javafx.beans.binding.Bindings.*;


public class LoginFormController {

    @FXML
    private PasswordField txtFieldPw;

    @FXML
    private TextField txtFieldUid;

    @FXML
    private ToggleButton btnToogle;

    @FXML
    private Label lblShownPw;

    @FXML
    private AnchorPane rootNode;


    @FXML
    void PwFieldKeyTyped(KeyEvent event) {
        lblShownPw.textProperty().bind(Bindings.concat(txtFieldPw.getText()));
    }

    @FXML
    void tooggleBtnOnAction(ActionEvent event) {

        if (btnToogle.isSelected()){
            lblShownPw.setVisible(true);
            lblShownPw.textProperty().bind(Bindings.concat(txtFieldPw.getText()));
            btnToogle.setText("Hide");
        }else{
            lblShownPw.setVisible(false);
            btnToogle.setText("Show");
        }
    }


    @FXML
    void OnForgotPassword(MouseEvent event) {




    }

    @FXML
    void onCreateAccount(MouseEvent event) {

    }

    @FXML
    void onActionLogin(MouseEvent event) throws IOException {

        String userId = txtFieldUid.getText();
        String password = txtFieldPw.getText();

        var dto = new UserDto(userId, password);
        var model = new LoginModel();

        try{
            String txt = model.checkLoginDetails(dto);
            if (txt.equals("correct password")){

                Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/dash_Board.fxml"));
                Scene scene = new Scene(rootNode);
                Stage stage = (Stage) this.rootNode.getScene().getWindow();
                stage.setTitle("Search Page");
                stage.setScene(scene);
                stage.centerOnScreen();
            }
            if (txt.equals("incorrect password")){
                new Alert(Alert.AlertType.ERROR,"Invalid Password !!").show();
            }
            if (txt.equals("Not Enter Password")){
                new Alert(Alert.AlertType.INFORMATION,"Please enter Password !!").show();
            }
            if (txt.equals("No enter userID")){
                new Alert(Alert.AlertType.INFORMATION,"Please Enter the User Id !!").show();
            }

        }catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Invalid User Id !!").show();
        }
    }
}
