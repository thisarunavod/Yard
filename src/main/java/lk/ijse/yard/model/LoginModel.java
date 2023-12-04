package lk.ijse.yard.model;

import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    public String checkLoginDetails(UserDto dto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT user_id FROM user WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getUserId());

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
//            System.out.println(resultSet.getString(1));
//            return true;
             String sql2 = "SELECT password FROM user WHERE user_id = ?";
             PreparedStatement pstm2 = connection.prepareStatement(sql2);
             pstm2.setString(1,dto.getUserId());

             ResultSet resultSet2 = pstm2.executeQuery();
             if (resultSet2.next()){
                 if (resultSet2.getString(1).equals(dto.getPassword())){ return "correct password";}
                 else if (dto.getPassword().equals("")){ return "Not Enter Password"; }
                 else { return  "incorrect password";}
             }else{
                 return  "incorrect password";
             }

        }else if(dto.getUserId().equals("")){
            return "No enter userID";
        }else{
            resultSet.getString(1);
            return "Invalid User Id";
        }
    }


    public boolean isCorrectUserID(String userID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT user_id FROM user WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,userID);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){ return true;}
        return false;
    }

    public boolean setUserPassword(String ID, String password) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE user SET password = ? WHERE user_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,password);
        pstm.setString(2,ID);

        return pstm.executeUpdate() > 0;

    }
}
