package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.EmployeeDto;
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

    private final EmployeeModel employeeModel = new EmployeeModel();

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
                 availbleDtolist = employeeModel.loadAllDriverse();
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

        String sql = "SELECT COUNT(*) AS noOfRaws FROM vehicle";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            if (resultSet.getInt(1) == 0) { return 0; }
            else  { return 1 ;}
        }
        return 0;

    }


}
