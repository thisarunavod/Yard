package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.export.SimpleRtfReportConfiguration;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialIssuedDetailsDto {

    private String issuedID;
    private String materialID;
    private String projectNo;
    private double issuedQty;
    private String vehicleID;
    private LocalDate issuedDate;

}
