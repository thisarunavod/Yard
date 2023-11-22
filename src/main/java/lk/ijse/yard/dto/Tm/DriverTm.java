package lk.ijse.yard.dto.Tm;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverTm {

    private String driverID ;
    private String driverName;
    private JFXToggleButton btn;


}
