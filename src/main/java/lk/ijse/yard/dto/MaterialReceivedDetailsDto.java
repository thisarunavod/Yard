package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialReceivedDetailsDto {

    private String materialID;
    private String receivedId;
    private LocalDate receivedDate;
    private String supId;
    private String materialName;
    private String materialType;
    private double receivedQty ;
    private String unit;

}
