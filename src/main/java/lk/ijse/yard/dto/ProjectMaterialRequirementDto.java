package lk.ijse.yard.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectMaterialRequirementDto {

    private String projectNO ;
    private String projectName;
    private String materialID;
    private double issuedQty;
    private double reqQty;
    private String unit;

}
