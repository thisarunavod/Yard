package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserModel {

    public boolean addUser(UserDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user VALUES (?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getUserId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getPassword());

        return pstm.executeUpdate() > 0 ;
    }
}
