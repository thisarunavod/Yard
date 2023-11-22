package lk.ijse.yard.model;

import javafx.scene.control.Alert;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialReceivedDetailsDto;
import lk.ijse.yard.dto.SupplierDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaterialReceivedDetailsModel {


    public boolean addMaterialReceivedDetails(MaterialReceivedDetailsDto receivedDetailsDto) throws SQLException {

        boolean isAdd = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyAddMRDetails = addMRDetails(receivedDetailsDto);
//            System.out.println("1 step "+firstlyAddMRDetails);
            if (firstlyAddMRDetails){
                boolean secondlyUpdateMaterial = MaterialModel.updateQty(receivedDetailsDto,"+");
                if (secondlyUpdateMaterial){
                    connection.commit();
                    isAdd = true;
                }
            }

        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } finally {
            connection.setAutoCommit(true);
        }
        return  isAdd;
    }

    public List<MaterialReceivedDetailsDto> loadAllMReceivedDetails() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM material_receive_details";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<MaterialReceivedDetailsDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var receivedDto = new MaterialReceivedDetailsDto(
                    resultSet.getString(3),
                    resultSet.getString(1),
                    resultSet.getDate(8).toLocalDate(),
                    resultSet.getString(2),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getDouble(6),
                    resultSet.getString(7)
            );

            dtoList.add(receivedDto);

        }
        return dtoList;
    }


    private boolean addMRDetails(MaterialReceivedDetailsDto receivedDetailsDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO material_receive_details VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,receivedDetailsDto.getReceivedId());
        pstm.setString(2,receivedDetailsDto.getSupId());
        pstm.setString(3,receivedDetailsDto.getMaterialID());
        pstm.setString(4,receivedDetailsDto.getMaterialName());
        pstm.setString(5,receivedDetailsDto.getMaterialType());
        pstm.setString(6,String.valueOf(receivedDetailsDto.getReceivedQty()));
        pstm.setString(7,receivedDetailsDto.getUnit());
        pstm.setDate(8, Date.valueOf(receivedDetailsDto.getReceivedDate()));

        return pstm.executeUpdate() > 0;

    }

    public boolean deletReceivedDetails(MaterialReceivedDetailsDto dto) throws SQLException {

        boolean isDelete  = false;
        Connection connection = null;

        try {

            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstDeleteMRdetails = deleteMRdetails(dto);
            if (firstDeleteMRdetails){
                boolean secondlyChangeMaterialQty = MaterialModel.updateQty(dto,"-");
                if (secondlyChangeMaterialQty){
                    connection.commit();
                    isDelete = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }
        return isDelete;
    }

    private boolean deleteMRdetails(MaterialReceivedDetailsDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM material_receive_details WHERE received_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getReceivedId());

        return  pstm.executeUpdate() > 0 ;

    }
}
