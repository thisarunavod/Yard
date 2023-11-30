package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleDto {

    private String vehicleId;
    private String vehicleName;
    private String empId;
    private String rootStatus;
    private String driverAvailability;
    private String removeORworking;

}
