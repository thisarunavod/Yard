package lk.ijse.yard.dto.Tm;

import javafx.scene.control.ProgressBar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectListTm {
    private String projectNO;
    private String projectName;
    private LocalDate completionDate;
    private ProgressBar progressCompletion;
    private String precentage;
}
