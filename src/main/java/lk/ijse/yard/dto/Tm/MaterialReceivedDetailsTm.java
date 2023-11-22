package lk.ijse.yard.dto.Tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MaterialReceivedDetailsTm {
    private String materialID;
    private String receivedId;
    private LocalDate receivedDate;
    private String supId;
    private String materialName;
    private String materialType;
    private double receivedQty ;
    private String unit;
    private Button btnUpdate;
    private Button btnDelete;

}
