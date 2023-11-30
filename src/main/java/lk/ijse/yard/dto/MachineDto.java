package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MachineDto {

    private String machineID;
    private String machineName;
    private String status;

    public MachineDto(String machineID , String machineName){
        this.machineID = machineID;
        this.machineName = machineName;
    }


}
