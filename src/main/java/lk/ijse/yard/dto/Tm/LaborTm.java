package lk.ijse.yard.dto.Tm;

import com.jfoenix.controls.JFXToggleButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LaborTm {

    private String laborID ;
    private String laborName;
    private JFXToggleButton btn;


}

