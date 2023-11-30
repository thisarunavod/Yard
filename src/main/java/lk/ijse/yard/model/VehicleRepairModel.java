package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.VehicleRepairDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepairModel {


    public boolean addRepairDetails(VehicleRepairDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO vehicle_repairs VALUES (?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getRepairID());
        pstm.setString(2,dto.getVehicleID());
        pstm.setString(3,dto.getDescription());
        pstm.setDouble(4,dto.getRepairCost());
        pstm.setDate(5, Date.valueOf(dto.getRepairDate()));

        return pstm.executeUpdate() > 0 ;

    }

    public List<VehicleRepairDto> loadAllRepairDetails() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM vehicle_repairs ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<VehicleRepairDto> dtoList = new ArrayList<>();
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            var repDto = new VehicleRepairDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getDate(5).toLocalDate()
            );

            dtoList.add(repDto);
        }
        return dtoList;
    }

    public boolean deletRepairDetails(String repairID) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM vehicle_repairs WHERE repair_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,repairID);

        return pstm.executeUpdate() > 0;

    }
}
