package lk.ijse.yard.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MaterialTm {

    private String materialID;
    private String MaterialName;
    private String type;
    private double qtyAvailable;
    private String unit;


}
