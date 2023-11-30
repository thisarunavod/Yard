package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleRepairDto {

    private String repairID;
    private String vehicleID;
    private String description;
    private double repairCost;
    private LocalDate repairDate;

}
