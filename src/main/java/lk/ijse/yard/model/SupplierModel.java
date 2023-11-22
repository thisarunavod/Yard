package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.SupplierDto;
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
public class SupplierModel {

    public int getRawCount() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS noOfRaws FROM suppliers";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            if (resultSet.getInt(1) == 0) { return 0; }
            else  { return 1 ;}
        }
        return 0;
    }

    public List<SupplierDto> loadAllSuppliers() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM suppliers";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<SupplierDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dtoSupplier = new SupplierDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            dtoList.add(dtoSupplier);
        }

        return dtoList;
    }

    public SupplierDto findOldSupplierDetails(String id) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *  FROM suppliers WHERE sup_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        ResultSet resultSet = pstm.executeQuery();

        SupplierDto supplierDto = null;

        if (resultSet.next()){
//            System.out.println(resultSet.getString(1)+" "+resultSet.getString(2));
            supplierDto = new SupplierDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
//            System.out.println(materialDto.getUnit());
        }
        return supplierDto;
    }

    public boolean updateSupplierDetails(SupplierDto dto ,String oldId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE suppliers SET sup_id = ?, company_name = ?, company_email = ?  WHERE sup_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getSupplierId());
        pstm.setString(2, dto.getCompanyName());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, oldId);

        return pstm.executeUpdate() > 0;
    }




    public boolean addSupplier(SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO suppliers VALUES (?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getSupplierId());
        pstm.setString(2, dto.getCompanyName());
        pstm.setString(3, dto.getEmail());
        int affectedRaw = pstm.executeUpdate();

        if (affectedRaw > 0){ return true; } else { return false;}
    }

    public boolean deleteSupplier(String supplierId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM suppliers WHERE sup_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, supplierId);
        return  pstm.executeUpdate() > 0 ;

    }
}
