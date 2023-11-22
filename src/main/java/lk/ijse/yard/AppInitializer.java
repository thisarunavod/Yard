package lk.ijse.yard;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);

        /*Pattern compile = Pattern.compile("^[\\w\\*\\$][\\w\\s\\-\\$]*(\\(\\d{1,}\\)){0,1}$");
        Matcher value = compile.matcher("sodvk");
        boolean mathches = value.matches();
        System.out.println(mathches);*/

    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/lk.ijse.yard/view/Login_form.fxml"));
        Scene scene = new Scene(rootNode);
        stage.setTitle("Login");

        stage.centerOnScreen();
        stage.setScene(scene);

        stage.show();


    }
}
