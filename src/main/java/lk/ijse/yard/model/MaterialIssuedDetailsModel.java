package lk.ijse.yard.model;

import javafx.scene.control.Alert;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.MaterialIssuedDetailsDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialIssuedDetailsModel {

    public boolean AddMaterialIssuedDetails(MaterialIssuedDetailsDto issuedDto) throws SQLException {
        boolean isAdded = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyUpdateMaterialTable = MaterialModel.updateQtyWIthIssued(issuedDto);
            if (firstlyUpdateMaterialTable){
                boolean secondlyUpdateVehicleTable = VehicleModel.changeRootStatusWithMaterialIssue(issuedDto,"ON_root");
                if (secondlyUpdateVehicleTable){
                    boolean thirdlyUpdateProjectMaterialRequirementTable = ProjectMaterialRequirementModel.updateIssueQty(issuedDto,"+");
                    if (thirdlyUpdateProjectMaterialRequirementTable){
                        if (addMaterialIssuedDetails(issuedDto)){
                            connection.commit();
                            isAdded = true;
                        }
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

    private boolean addMaterialIssuedDetails(MaterialIssuedDetailsDto issuedDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO material_send_details VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, issuedDto.getIssuedID());
        pstm.setString(2,issuedDto.getMaterialID());
        pstm.setString(3,issuedDto.getProjectNo());
        pstm.setDouble(4,issuedDto.getIssuedQty());
        pstm.setString(5,issuedDto.getVehicleID());
        pstm.setDate(6, Date.valueOf(issuedDto.getIssuedDate()));

        return pstm.executeUpdate() > 0;

    }

    public List<MaterialIssuedDetailsDto> loadAllMIssuedDetails() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = " SELECT * FROM material_send_details ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<MaterialIssuedDetailsDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dto = new MaterialIssuedDetailsDto(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getDouble(4),
                resultSet.getString(5),
                resultSet.getDate(6).toLocalDate()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public boolean deletIssuedDetails(MaterialIssuedDetailsDto dto, MaterialDto materialDto, String vehicleName) throws SQLException {

        boolean isDelete  = false;
        Connection connection = null;

        try {

            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstDeleteMaterialIssueddetails = deleteMaterialIssuedDetails(dto);
            if (firstDeleteMaterialIssueddetails){
                boolean secondlyChangeMaterialQty = MaterialModel.updateQtyWithDeleteIssuedDetails(dto);
                if (secondlyChangeMaterialQty){
                    boolean thirdlyChangeMaterialRequirementQty = ProjectMaterialRequirementModel.updateIssueQty(dto, "-");
                    if (thirdlyChangeMaterialRequirementQty){
                        boolean fourthlyUpdateVehicleRootStates = VehicleModel.changeRootStatusWithMaterialIssue(dto , "ON_yard");
                        if (fourthlyUpdateVehicleRootStates){

                            connection.commit();
                            isDelete = true;

                        }
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }
        return isDelete;

    }

    private boolean deleteMaterialIssuedDetails(MaterialIssuedDetailsDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM material_send_details WHERE issue_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getIssuedID());

        return  pstm.executeUpdate() > 0 ;

    }
}
