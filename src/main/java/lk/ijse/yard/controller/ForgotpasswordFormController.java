package lk.ijse.yard.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lk.ijse.yard.model.LoginModel;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Pattern;

public class ForgotpasswordFormController {

    private final LoginModel loginModel = new LoginModel();

    @FXML
    private JFXTextArea txtFieldNewPassword;

    @FXML
    private JFXTextArea txtFieldOTP;

    @FXML
    private JFXTextArea txtfieldUserID;

    Random r = new Random();
    int number = r.nextInt(9000);
    int number1 = number + 1000;

    @FXML
    void OnActionSendOTP(ActionEvent event) throws MessagingException {
        EmailController.sendEmail("amithawerahena@gmail.com", "Yard System Verification", number1 + "");
    }

    @FXML
    void OnActionSetNewPassword(ActionEvent event) {

        boolean isValidUserDetails = validateFields();

        if (isValidUserDetails){

            try {
                String userID = txtfieldUserID.getText();
                boolean isCorrectUserName = loginModel.isCorrectUserID(userID);
                if (isCorrectUserName){
                    try {
                        if (number1 == Integer.parseInt(txtFieldOTP.getText())){

                            String password = txtFieldNewPassword.getText();
                            boolean addedNewPassword = loginModel.setUserPassword(userID,password);
                            if (addedNewPassword){ new Alert(Alert.AlertType.INFORMATION,"Password Reset Successfully").show();}

                        }else{
                            new Alert(Alert.AlertType.ERROR,"Incorect OTP !! ").show();
                        }
                    } catch (NumberFormatException e) {

                    }

                }else{
                    new Alert(Alert.AlertType.ERROR,"invalid User ID !!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private boolean validateFields() {

        String userID = txtfieldUserID.getText();
        boolean isValidUserID = Pattern.matches("^\\d",userID); //^\d+[A-Z]$
        if (!isValidUserID){ new Alert(Alert.AlertType.ERROR,"Invalid USER ID").show(); return false; }

        String password = txtFieldNewPassword.getText();
        boolean isValidPassword = Pattern.matches("^\\d",password); //^\d+[A-Z]$
        if (!isValidPassword){ new Alert(Alert.AlertType.ERROR,"Invalid PASSWORD").show(); return false; }


        String OTP = txtFieldOTP.getText();
        boolean isValidOTP = Pattern.matches("^(1000|[1-9]\\d{3})",OTP);
        if (!isValidOTP){ new Alert(Alert.AlertType.ERROR,"Invalid OTP").show(); return false; }

        return true ;
    }
}
