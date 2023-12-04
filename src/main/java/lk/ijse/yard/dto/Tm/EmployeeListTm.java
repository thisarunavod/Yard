package lk.ijse.yard.dto.Tm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class EmployeeListTm {
    private String empID ;
    private String empName;
    private String empAddress;
    private String jobTitle;
    private String availability;
}
