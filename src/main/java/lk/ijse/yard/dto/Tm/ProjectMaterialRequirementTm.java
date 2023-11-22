package lk.ijse.yard.dto.Tm;

import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectMaterialRequirementTm {
    private String materialID;
    private String materialName;
    private TextField reqQty;
    private String unit;
}
