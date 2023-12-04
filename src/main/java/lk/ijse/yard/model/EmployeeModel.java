package lk.ijse.yard.model;

import javafx.scene.control.Alert;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.VehicleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static List<EmployeeDto> loadAllDriverse() throws SQLException {

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

    public boolean changeAvailability(EmployeeDto dto, String availability) throws SQLException {
        boolean isChangeCorrectly = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyChangeEmployeTable = setAvailabilityforEmployeTable(dto , availability);
            if (firstlyChangeEmployeTable){
                boolean secondlyUpdateDriverTable = VehicleModel.setDriverAvailability(dto , availability);
                if (secondlyUpdateDriverTable){
                    connection.commit();
                    isChangeCorrectly = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } finally {
            connection.setAutoCommit(true);
        }
        return  isChangeCorrectly;


    }

    private boolean setAvailabilityforEmployeTable(EmployeeDto dto, String availability) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employe SET availability = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, availability);
        pstm.setString(2, dto.getEmpID());

        return pstm.executeUpdate() > 0;

    }


    public String getDriverAvailability(String empId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT availability FROM employe WHERE e_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,empId);

        ResultSet resultSet = pstm.executeQuery();
        String availability = "";

        while (resultSet.next()){
            availability = resultSet.getString(1);
        }

        return availability;

    }

    public EmployeeDto getEmployeeDetails(String empId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employe WHERE e_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,empId);

        ResultSet resultSet = pstm.executeQuery();

        EmployeeDto dto = null;

        while (resultSet.next()){
            dto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return dto;
    }


    public boolean deleteRegisterDriverse(String empId) throws SQLException {
        boolean isDelete = false;
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyUpdateVehicleTable = VehicleModel.updateProcessDriverDeletion(empId);

            if (firstlyUpdateVehicleTable){
                boolean secondlyDeleteEmploye = deleteEmployee(empId);
                if (secondlyDeleteEmploye){
                    connection.commit();
                    isDelete = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } finally {
            connection.setAutoCommit(true);
        }
        return  isDelete;

    }

    public boolean deleteEmployee(String empId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM employe WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, empId);

        return  pstm.executeUpdate() > 0 ;

    }

    public static String getName(String empId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT e_name FROM employe WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, empId);
        ResultSet resultSet = pstm.executeQuery();

        String name = null;
        if (resultSet.next()) { name = resultSet.getString(1);}

        return  name;
    }


    public List<EmployeeDto> loadAllLabors() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employe WHERE job_title = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,"LABOR");
        ResultSet resultSet = pstm.executeQuery();

        List<EmployeeDto> dtoList = new ArrayList<>();
        while (resultSet.next()){

            var dtoLabor = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            dtoList.add(dtoLabor);
        }

        return dtoList;
    }
}
