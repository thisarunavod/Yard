package lk.ijse.yard.dto.Tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data


public class MaterialIssuedDetailsTm {
    private LocalDate issuedDate;
    private String pNO;
    private String issuedID;
    private String mID;
    private String materialName;
    private String materialType;
    private double qty;
    private String unit;
    private String vehicleID;
    private String vehicleName;
    private Button btnUpdate;
    private Button btnDelete;

}
