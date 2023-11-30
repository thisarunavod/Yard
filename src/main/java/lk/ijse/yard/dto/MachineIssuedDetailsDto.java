package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MachineIssuedDetailsDto {

    private String machineIssuedID;
    private String machineID;
    private String projectNO;
    private String vehicleID;
    private LocalDate issueDate;

}
