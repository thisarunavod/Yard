package lk.ijse.yard.model;


import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.MaterialIssuedDetailsDto;
import lk.ijse.yard.dto.MaterialReceivedDetailsDto;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MaterialModel {


    public static boolean updateQty(MaterialReceivedDetailsDto dto, String comment) throws SQLException {

        String sql ="";
        if (comment.equals("+")){
            sql = "UPDATE material SET qty_available = qty_available + ? WHERE m_id = ? ";
        }else{
            sql = "UPDATE material SET qty_available = qty_available - ? WHERE m_id = ? ";
        }

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDouble(1,dto.getReceivedQty());
        pstm.setString(2,dto.getMaterialID());

        return pstm.executeUpdate() > 0 ;

    }



    public static boolean updateQtyWIthIssued(MaterialIssuedDetailsDto issuedDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE material SET qty_available = qty_available - ? WHERE m_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDouble(1 , issuedDto.getIssuedQty());
        pstm.setString(2, issuedDto.getMaterialID());

        return pstm.executeUpdate() > 0 ;

    }

    public static boolean updateQtyWithDeleteIssuedDetails(MaterialIssuedDetailsDto dto) throws SQLException {

        String sql = "UPDATE material SET qty_available = qty_available + ? WHERE m_id = ? ";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDouble(1,dto.getIssuedQty());
        pstm.setString(2,dto.getMaterialID());

        return pstm.executeUpdate() > 0 ;

    }


    public MaterialDto findOldMaterialDetails(String id) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *  FROM material WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        ResultSet resultSet = pstm.executeQuery();

        MaterialDto materialDto = null;

        if (resultSet.next()){
//            System.out.println(resultSet.getString(1)+" "+resultSet.getString(2));
            materialDto = new MaterialDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
//            System.out.println(materialDto.getUnit());
        }
        return materialDto;
    }


    public  List<MaterialDto> loadAllMaterials() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM material";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<MaterialDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dtoMaterial = new MaterialDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
            dtoList.add(dtoMaterial);
        }

        return dtoList;
    }

    public int getRawCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS noOfRaws FROM material";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            if (resultSet.getInt(1) == 0) { return 0; }
            else  { return 1 ;}
        }
        return 0;

    }


    public boolean addMatrials(MaterialDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO material VALUES (?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getMaterialID());
        pstm.setString(2,dto.getMaterialName());
        pstm.setString(3, dto.getType());
        pstm.setDouble(4,dto.getQtyAvailable());
        pstm.setString(5,dto.getUnit());
        int affectedRaw = pstm.executeUpdate();

        if (affectedRaw > 0){ return true; } else { return false;}

    }

    public boolean updateMaterialDetails(MaterialDto dto ,String oldId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE material SET m_id = ?, material_name = ?, type = ? ,qty_available = ?, unit = ? WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getMaterialID());
        pstm.setString(2, dto.getMaterialName());
        pstm.setString(3, dto.getType());
        pstm.setDouble(4,dto.getQtyAvailable());
        pstm.setString(5,dto.getUnit());
        pstm.setString(6,oldId);

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteMaterial(String id) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM material WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return  pstm.executeUpdate() > 0 ;
    }


    public double chekStock(String materialID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT qty_available FROM material WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,materialID);
        ResultSet resultSet = pstm.executeQuery();
        double qty = 0.0;
        if (resultSet.next()){ qty = resultSet.getDouble(1);}

        return qty;
    }



}
