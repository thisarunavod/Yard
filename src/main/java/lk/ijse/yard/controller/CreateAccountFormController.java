package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lk.ijse.yard.dto.UserDto;
import lk.ijse.yard.model.UserModel;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Pattern;

public class CreateAccountFormController {

    private final UserModel userModel = new UserModel();

    @FXML
    private JFXTextArea txtOTP;

    @FXML
    private JFXTextArea txtUserEmail;

    @FXML
    private JFXTextArea txtUserID;

    @FXML
    private JFXTextArea txtUserName;

    @FXML
    private JFXTextArea txtUserPassword;

    Random r = new Random();
    int number = r.nextInt(9000);
    int number1 = number + 1000;

    @FXML
    void OnActionCreateAccount(ActionEvent event) {
        boolean isValidUserDetails = validUserDetails();
        if (isValidUserDetails){
            try {
                if (number1 == Integer.parseInt(txtOTP.getText())){
                    String userID = txtUserID.getText();
                    String userName = txtUserName.getText();
                    String email = txtUserEmail.getText();
                    String password = txtUserPassword.getText();

                    var dto  = new UserDto(userID,userName,email,password);
                    boolean isAddUser = userModel.addUser(dto);
                    if (isAddUser){ new Alert(Alert.AlertType.INFORMATION,"User Added Successfully").show();}

                }else{
                    new Alert(Alert.AlertType.ERROR,"Incorrect OTP !!").show();
                }
            } catch (NumberFormatException | SQLException e) {

            }
        }
    }

    @FXML
    void OnActionSendOTP(ActionEvent event) throws MessagingException {
        EmailController.sendEmail("amithawerahena@gmail.com", "Yard System Verification", number1 + "");
    }


    private boolean validUserDetails(){

        String userID = txtUserID.getText();
        boolean validID = Pattern.matches("^.*V$",userID);
        if (!validID){ new Alert(Alert.AlertType.ERROR,"invalid User ID !").show(); return false;}

//
        String userName = txtUserName.getText();
        boolean validName = Pattern.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+",userName);
        if (!validName){ new Alert(Alert.AlertType.ERROR,"invalid User Name !").show(); return false;}

        String userEmail = txtUserEmail.getText();
        boolean validEmail = Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",userEmail);
        if (!validEmail){ new Alert(Alert.AlertType.ERROR,"invalid User Email !").show(); return false;}

        String OTP = txtOTP.getText();
        boolean isValidOTP = Pattern.matches("^(1000|[1-9]\\d{3})",OTP);
        if (!isValidOTP){ new Alert(Alert.AlertType.ERROR,"Invalid OTP").show(); return false; }

        return true;


    }
}



