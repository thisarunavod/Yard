package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.MaterialDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.digester.annotations.rules.BeanPropertySetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class EmployeeModel {


    public static int getRawCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS noOfRaws FROM employe";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            if (resultSet.getInt(1) == 0) { return 0; }
            else  { return 1 ;}
        }
        return 0;
    }

    public boolean addEmployee(EmployeeDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO employe VALUES (?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getEmpID());
        pstm.setString(2,dto.getEmpName());
        pstm.setString(3,dto.getEmpAddress());
        pstm.setString(4,dto.getJobTitle());
        pstm.setString(5, dto.getAvailability());

        int affectedRaw = pstm.executeUpdate();

        return affectedRaw > 0;

    }

    public List<EmployeeDto> loadAllEmployees() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employe";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<EmployeeDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dtoEmployee = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            dtoList.add(dtoEmployee);
        }

        return dtoList;

    }

    public EmployeeDto findOldEmployeeDetails(String empID) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employe WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, empID);

        ResultSet resultSet = pstm.executeQuery();

        EmployeeDto employeeDto = null;

        if (resultSet.next()){

            employeeDto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)

            );

        }
        return employeeDto;

    }

    public boolean updateEmployee(EmployeeDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE employe SET e_name = ?, e_address = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getEmpName());
        pstm.setString(2, dto.getEmpAddress());
        pstm.setString(3, dto.getEmpID());

        return pstm.executeUpdate() > 0;



    }

    public List<EmployeeDto> loadAllDriverse() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employe WHERE job_title = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,"DRIVER");
        ResultSet resultSet = pstm.executeQuery();

        List<EmployeeDto> dtoList = new ArrayList<>();
        while (resultSet.next()){

            var dtoDriver = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            dtoList.add(dtoDriver);
        }

        return dtoList;
    }

    public boolean changeAvailability(EmployeeDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employe SET availability = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, "AV");
        pstm.setString(2, dto.getEmpID());

        return pstm.executeUpdate() > 0;

    }

    public boolean changeNonAvailability(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employe SET availability = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, "NA");
        pstm.setString(2, dto.getEmpID());

        return pstm.executeUpdate() > 0;

    }


}
