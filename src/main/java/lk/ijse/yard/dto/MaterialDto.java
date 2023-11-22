package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialDto {

    private String materialID;
    private String MaterialName;
    private String type;
    private double qtyAvailable;
    private String unit;

}
