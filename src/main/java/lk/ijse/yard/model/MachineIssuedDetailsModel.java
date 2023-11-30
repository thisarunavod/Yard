package lk.ijse.yard.model;

import javafx.scene.control.Alert;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MachineIssuedDetailsDto;
import lk.ijse.yard.dto.MaterialReceivedDetailsDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineIssuedDetailsModel {

    public boolean addIssueDetails(MachineIssuedDetailsDto dto) throws SQLException {

        boolean isAdded = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyUpdateMachineTable = MachineModel.updateStatusWIthIssued(dto,"ON_project");
            if (firstlyUpdateMachineTable){
               boolean updateVehicleRoot = VehicleModel.changeRootStatusWithMachineIssue(dto.getVehicleID(),"ON_root");
               if (updateVehicleRoot){
                   boolean thirdlyAddIssuedDetails = addMachineIssuedDetails(dto);
                   if (thirdlyAddIssuedDetails){
                       connection.commit();
                       isAdded = true;
                   }
               }
            }
        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } finally {
            connection.setAutoCommit(true);
        }
        return  isAdded;

    }

    private boolean addMachineIssuedDetails(MachineIssuedDetailsDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO machine_send_details VALUES (?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getMachineIssuedID());
        pstm.setString(2,dto.getMachineID());
        pstm.setString(3,dto.getProjectNO());
        pstm.setString(4,dto.getVehicleID());
        pstm.setDate(5, Date.valueOf(dto.getIssueDate()));

        return pstm.executeUpdate() > 0;

    }

    public List<MachineIssuedDetailsDto> loadAllMachineIssuedDetails() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine_send_details ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<MachineIssuedDetailsDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var machineIssuedDetailsDto = new MachineIssuedDetailsDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDate(5).toLocalDate()
            );
            dtoList.add(machineIssuedDetailsDto);
        }
        return dtoList;

    }

    public boolean deletReceivedDetails(MachineIssuedDetailsDto dto) throws SQLException {

        boolean isDeleted = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyUpdateMachineTable = MachineModel.updateStatusWIthIssued(dto,"ON_yard");
            if (firstlyUpdateMachineTable){
                boolean updateVehicleRoot = VehicleModel.changeRootStatusWithMachineIssue(dto.getVehicleID(),"ON_yard");
                if (updateVehicleRoot){
                    boolean thirdlyDeletedIssuedDetails = deleteMachineIssuedDetails(dto);
                    if (thirdlyDeletedIssuedDetails){
                        connection.commit();
                        isDeleted = true;
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } finally {
            connection.setAutoCommit(true);
        }
        return  isDeleted;

    }

    private boolean deleteMachineIssuedDetails(MachineIssuedDetailsDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM machine_send_details WHERE issue_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getMachineIssuedID());

        return pstm.executeUpdate() > 0 ;

    }
}
