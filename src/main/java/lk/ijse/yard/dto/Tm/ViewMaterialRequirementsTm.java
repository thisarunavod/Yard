package lk.ijse.yard.dto.Tm;

import javafx.scene.control.ProgressBar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewMaterialRequirementsTm {

    private String materialID;
    private String materialName;
    private String materialType;
    private double issuedQty;
    private double reqQty;
    private double moreReqQty;
    private String unit;
    private ProgressBar suppliments;

}
