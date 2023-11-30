package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.EmployeeDto;
import lk.ijse.yard.dto.MaterialIssuedDetailsDto;
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
public class VehicleModel {



    public static boolean setDriverAvailability(EmployeeDto dto, String availability) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET driver_availability = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,availability);
        pstm.setString(2,dto.getEmpID());


        pstm.executeUpdate();
        return true;
    }

    public static boolean changeRootStatus(String empId, String status) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET root_status = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,status);
        pstm.setString(2,empId);

        return pstm.executeUpdate() > 0 ;
    }

    public static String getRootStatus(String empId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT root_status FROM vehicle WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,empId);
        ResultSet resultSet = pstm.executeQuery();
        String status = null;
        if (resultSet.next()){ status = resultSet.getString(1);}
        return status ;

    }

    public static boolean isOnYard(String empID) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT root_status FROM vehicle WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,empID);

        ResultSet resultSet = pstm.executeQuery();
        boolean isYard = false;
        if (resultSet.next()){
            if (resultSet.getString(1).equals("ON_yard")) { isYard = true; }
        }

        return isYard;

    }

    public static boolean changeRootStatusWithMaterialIssue(MaterialIssuedDetailsDto issuedDto , String status) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET root_status = ? WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, status);
        pstm.setString(2, issuedDto.getVehicleID());

        return pstm.executeUpdate() > 0 ;

    }

    public static boolean changeRootStatusWithMachineIssue(String vehicleID , String status) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET root_status = ? WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, status);
        pstm.setString(2, vehicleID);

        return pstm.executeUpdate() > 0 ;

    }

    public List<EmployeeDto> loadAllAvailableDriverse() throws SQLException {

        int vehicleRawCount = findvehicleRawCount();
        List<EmployeeDto> availbleDtolist = null;

        if (vehicleRawCount > 0){
            try {
                String q = findAddedDriverse();
                availbleDtolist = findAvailableDto(q);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                 availbleDtolist = EmployeeModel.loadAllDriverse();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return availbleDtolist;
    }

    private List<EmployeeDto> findAvailableDto(String q) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employe WHERE e_id NOT IN "+q+" AND job_title = 'DRIVER'" ;
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();


        List<EmployeeDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    private String findAddedDriverse() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT e_id FROM vehicle";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List <String> query = new ArrayList<>();
        while (resultSet.next()){
            query.add(resultSet.getString(1));
        }

        String q = "(";
        for (int i = 0; i < query.size(); i++) {
            if (i == query.size() - 1){
                q += "'"+query.get(i)+"'"+")";
                break;
            }
            q += "'"+query.get(i)+"'"+",";
        }
        return q;

    }

    private int findvehicleRawCount() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS noOfRaws FROM vehicle WHERE remove_or_working = 'working' ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            if (resultSet.getInt(1) == 0) { return 0; }
            else  { return 1 ;}
        }
        return 0;

    }


    public boolean addVehicle(VehicleDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO vehicle VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getVehicleId());
        pstm.setString(2,dto.getVehicleName());
        pstm.setString(3, dto.getEmpId());
        pstm.setString(4,dto.getRootStatus());
        pstm.setString(5,dto.getDriverAvailability());
        pstm.setString(6,dto.getRemoveORworking());

        int affectedRaw = pstm.executeUpdate();

        if (affectedRaw > 0){ return true; } else { return false;}

    }

    public List<VehicleDto> loadAllVehicleIDs() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM vehicle WHERE remove_or_working = 'working' ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<VehicleDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dtoVehicle = new VehicleDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            dtoList.add(dtoVehicle);
        }

        return dtoList;
    }

    public String getAddedDriverID(String vehicleID) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT e_id FROM vehicle WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,vehicleID);

        ResultSet resultSet = pstm.executeQuery();
        String id = null;
        while (resultSet.next()){
            id = resultSet.getString(1);
        }
        return id ;
    }

    public boolean updateVehicleWithDriver(VehicleDto vehicleDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET v_name = ?, e_id = ? , driver_availability = ? WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,vehicleDto.getVehicleName());
        pstm.setString(2,vehicleDto.getEmpId());
        pstm.setString(3,vehicleDto.getDriverAvailability());
        pstm.setString(4,vehicleDto.getVehicleId());

        return  pstm.executeUpdate() > 0 ;
    }

    public boolean updateVehicleNormal(VehicleDto vehicleDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET v_name = ?  WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,vehicleDto.getVehicleName());
        pstm.setString(2,vehicleDto.getVehicleId());

        return  pstm.executeUpdate() > 0 ;

    }

    public String getVehicleName(String vehicleID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT v_name FROM vehicle  WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,vehicleID);

        String name = null;
        ResultSet resultSet =pstm.executeQuery();
        while (resultSet.next()){ name = resultSet.getString(1); }

        return name;
    }

    public boolean isVehicleRegisterDriver(String empId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT e_id FROM vehicle  WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,empId);

        ResultSet resultSet = pstm.executeQuery();
        boolean isRegisterDriver = true;

        if (resultSet.next() == false){  isRegisterDriver = false; }

        return isRegisterDriver;

    }

    public static boolean updateProcessDriverDeletion(String empId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET  e_id = ? , driver_availability = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,null);
        pstm.setString(2,null);
        pstm.setString(3,empId);

        return pstm.executeUpdate() > 0;

    }

    public boolean removeVehicle(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE vehicle SET  e_id = ? ,root_status = ? ,driver_availability = ? , remove_or_working = ?  WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,null);
        pstm.setString(2,null);
        pstm.setString(3,null);
        pstm.setString(4,"remove");
        pstm.setString(5,id);

        return pstm.executeUpdate() > 0 ;
    }

    public List<VehicleDto> loadAllVehicles() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        List<VehicleDto> dtoList = new ArrayList<>();

        String sql = "SELECT * FROM vehicle WHERE remove_or_working = 'working'";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            var vehicleDto = new VehicleDto(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6)
            );

            dtoList.add(vehicleDto);
        }
        return  dtoList;

    }

    public List<VehicleDto> loadAllDriverAvailbleVehicles() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vehicle WHERE  driver_availability LIKE 'AV%' ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<VehicleDto> dtoList  = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            var dtoVehicle = new VehicleDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            dtoList.add(dtoVehicle);
        }

        return dtoList;
    }


    public List<VehicleDto> loadAllAvailbleVehiclesForUse() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vehicle WHERE  driver_availability LIKE 'AV%' AND root_status = 'ON_yard' ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<VehicleDto> dtoList  = new ArrayList<>();
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            var dtoVehicle = new VehicleDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            dtoList.add(dtoVehicle);
        }

        return dtoList;

    }


    public boolean checkVehicleOnRoot(String vehicleID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT root_status FROM vehicle  WHERE v_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,vehicleID);

        ResultSet resultSet = pstm.executeQuery();
        boolean onRoot = false;
        if (resultSet.next()){ if (resultSet.getString(1).equals("ON_root")) { onRoot = true;}}
        return  onRoot;
    }
}
