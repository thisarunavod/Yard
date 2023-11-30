package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MachineDto;
import lk.ijse.yard.dto.MachineIssuedDetailsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MachineModel {


    public static boolean updateStatusWIthIssued(MachineIssuedDetailsDto dto,String status) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE machine SET status = ? WHERE machine_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,status);
        pstm.setString(2,dto.getMachineID());

        return pstm.executeUpdate() > 0;
    }

    public boolean addMachine(MachineDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO machine VALUES (?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getMachineID());
        pstm.setString(2,dto.getMachineName());
        pstm.setString(3,dto.getStatus());

        return pstm.executeUpdate() > 0;

    }

    public List<MachineDto> loadAllMachines() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine WHERE status NOT IN ('remove')";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<MachineDto> dtoList = new ArrayList<>();
        while (resultSet.next()){
            var machineDto = new MachineDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );

            dtoList.add(machineDto);
        }
        return dtoList;
    }

    public MachineDto findOldMaterialDetails(String machineId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine WHERE machine_id = ? AND status NOT IN ('remove')";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,machineId);

        ResultSet resultSet = pstm.executeQuery();
        MachineDto dto = null ;
        while (resultSet.next()){
            dto = new MachineDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
        }
        return dto;
    }

    public boolean updateMachineDetails(MachineDto dto, String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE machine SET machine_id = ? ,machine_name = ? WHERE machine_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getMachineID());
        pstm.setString(2,dto.getMachineName());
        pstm.setString(3,id);

        return pstm.executeUpdate() > 0;

    }

    public boolean removeMachine( String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE machine SET status = 'remove'  WHERE machine_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        return pstm.executeUpdate() > 0 ;

    }

    public boolean checkStatus(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT status FROM machine  WHERE machine_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        ResultSet resultSet = pstm.executeQuery();
        boolean isOutofYard = false;
        if (resultSet.next()){  isOutofYard = resultSet.getString(1).equals("ON_project");}

        return isOutofYard;
    }

    public boolean changeStatus(MachineDto dto, String status) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE machine SET status = ?  WHERE machine_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,status);
        pstm.setString(2,dto.getMachineID());

        return pstm.executeUpdate() > 0 ;


    }

    public List<MachineDto> loadAllAvailableMachines() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine WHERE status NOT IN ('remove','ON_project') ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<MachineDto> dtoList = new ArrayList<>();
        while (resultSet.next()){
            var machineDto = new MachineDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );

            dtoList.add(machineDto);
        }
        return dtoList;

    }



    public String getMachineName(String mID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT machine_name FROM machine  WHERE machine_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,mID);
        ResultSet resultSet = pstm.executeQuery();

        String name = "";

        if (resultSet.next()){ name = resultSet.getString(1); }

        return name;

    }
}
