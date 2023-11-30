package lk.ijse.yard.dto.Tm;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.ToggleButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MachineListTm {

    private String machineID;
    private String machineName;
    private JFXToggleButton status;

}
