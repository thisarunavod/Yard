package lk.ijse.yard.dto;

import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDto {

    private String projectNO;
    private String projectName;
    private String loacation ;
    private LocalDate completionDate;

}
