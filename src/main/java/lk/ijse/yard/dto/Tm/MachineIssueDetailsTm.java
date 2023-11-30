package lk.ijse.yard.dto.Tm;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MachineIssueDetailsTm {
    private LocalDate issuedDate;
    private String projectNO;
    private String machineIssuedID;
    private String machineID;
    private String machineName;
    private String vehicleID;
    private String vehicleName;
    private Button del;

}
