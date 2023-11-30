package lk.ijse.yard.dto.Tm;

import com.jfoenix.controls.JFXToggleButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvailbleVehicleTm {

    private String vehicleID ;
    private String vehicleName;
    private String empID;
    private  String empName;
    private  JFXToggleButton btn;

}
