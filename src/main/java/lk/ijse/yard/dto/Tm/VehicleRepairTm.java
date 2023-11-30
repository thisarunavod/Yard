package lk.ijse.yard.dto.Tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleRepairTm {

    private String repID;
    private String vehicleID;
    private String vehicleName;
    private String description;
    private double cost;
    private LocalDate repairDate;
    private Button btnDelete;

}
