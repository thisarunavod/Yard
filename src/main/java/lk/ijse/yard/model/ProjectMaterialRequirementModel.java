package lk.ijse.yard.model;

import com.google.protobuf.DoubleValue;
import javafx.collections.ObservableList;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.MaterialIssuedDetailsDto;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.dto.ProjectMaterialRequirementDto;
import lk.ijse.yard.dto.Tm.ProjectMaterialRequirementTm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMaterialRequirementModel {



    public static boolean AddProjectMaterialRequirement(ProjectDto dto, ObservableList<ProjectMaterialRequirementTm> obList) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO project_material_requirements VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        int affectedRaw = 0;
        for (int i = 0; i < obList.size(); i++) {


            pstm.setString(1,dto.getProjectNO());
            pstm.setString(2,dto.getProjectName());
            pstm.setString(3,obList.get(i).getMaterialID());
            pstm.setDouble(4,0.0);
            pstm.setDouble(5, (Double.valueOf(obList.get(i).getReqQty().getText())));
            pstm.setString(6, obList.get(i).getUnit());

            affectedRaw = affectedRaw + pstm.executeUpdate();

        }

        return affectedRaw > 0 ;

    }

    public static String getRequiredQty(String projectNO, String materialID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql =" SELECT required_qty FROM project_material_requirements WHERE p_no = ? AND m_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,projectNO);
        pstm.setString(2,materialID);

        ResultSet resultSet = pstm.executeQuery();
        String v = "0";
        while (resultSet.next()) {  v  = resultSet.getString(1); }
        return v;
    }

    public static boolean updateRequirement(ProjectDto projectDto, ObservableList<ProjectMaterialRequirementTm> obList) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE project_material_requirements SET p_name = ?, required_qty = ?  WHERE p_no = ? AND m_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int affectedRaw = 0;
        for (int i = 0; i < obList.size(); i++) {

            pstm.setString(1,projectDto.getProjectName());
            pstm.setDouble(2, (Double.valueOf(obList.get(i).getReqQty().getText())));
            pstm.setString(3,projectDto.getProjectNO());

            setNewAddedMaterial(projectDto,obList.get(i).getMaterialID(),obList.get(i).getUnit());

            pstm.setString(4,obList.get(i).getMaterialID());
            System.out.println(obList.get(i).getMaterialID());
            System.out.println(Double.valueOf(obList.get(i).getReqQty().getText()));
            affectedRaw = affectedRaw + pstm.executeUpdate();

        }

        return affectedRaw > 0 ;

    }

    public static boolean deleteRequirements(ProjectDto projectDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM project_material_requirements WHERE p_no = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, projectDto.getProjectNO());

        return  pstm.executeUpdate() > 0 ;

    }

    public static boolean updateIssueQty(MaterialIssuedDetailsDto issuedDto ,String comment) throws SQLException {

        String sql ="";
        if (comment.equals("+")){
            sql = "UPDATE project_material_requirements SET issue_qty = issue_qty + ?  WHERE p_no = ? AND m_id = ? ";
        }else{
            sql = "UPDATE project_material_requirements SET issue_qty = issue_qty - ?  WHERE p_no = ? AND m_id = ? ";
        }

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDouble(1,issuedDto.getIssuedQty());
        pstm.setString(2,issuedDto.getProjectNo());
        pstm.setString(3,issuedDto.getMaterialID());

        return pstm.executeUpdate() > 0;
    }

    public static void setNewAddedMaterial(ProjectDto projectDto, String mID, String unit) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT p_no FROM project_material_requirements WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,mID);
        ResultSet resultSet = pstm.executeQuery();
        if (!resultSet.next()){
            setMaterial(projectDto , mID , unit);
        }

    }

    private static void setMaterial(ProjectDto dto, String mID , String unit) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO project_material_requirements VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getProjectNO());
        pstm.setString(2,dto.getProjectName());
        pstm.setString(3,mID);
        pstm.setDouble(4,0.0);
        pstm.setDouble(5, 0.0);
        pstm.setString(6, unit);

        pstm.executeUpdate();
    }

    public boolean checkNonRequiremets(String pNo, String materialID) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT required_qty FROM project_material_requirements WHERE p_no = ? AND m_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,pNo);
        pstm.setString(2,materialID);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){ return resultSet.getDouble(1) == 0 ;}
        else{ return true;}

    }

    public boolean checkRequiremetsQtyExceed(String pNo, String materialID , double qty) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT required_qty - issue_qty AS checkRange FROM project_material_requirements WHERE p_no = ? AND m_id = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,pNo);
        pstm.setString(2,materialID);
        ResultSet resultSet = pstm.executeQuery();
        boolean exceed = false;
        if (resultSet.next()) {

            exceed = !(resultSet.getDouble(1) > qty);
        }
        return exceed ;

    }

    public List<ProjectMaterialRequirementDto> loadRequirements(String projectNO) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = " SELECT * FROM project_material_requirements WHERE p_no = ? AND required_qty > 0 ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,projectNO);

        List<ProjectMaterialRequirementDto> dtoList = new ArrayList<>();
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            var dto  = new ProjectMaterialRequirementDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            );
            dtoList.add(dto);
        }

        return  dtoList;
    }
/*
    p_no VARCHAR(20) NOT NULL ,
    p_name VARCHAR(50) NOT NULL ,
    m_id VARCHAR(6) NOT NULL,
    issue_qty FLOAT(15) NOT NULL,
    required_qty FLOAT(15) NOT NULL,
    unit VARCHAR (6) NOT NULL,*/








    /*public static List<MaterialDto> loadMaterialRequirement(String projectNO) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql =" SELECT * FROM project_material_requirements WHERE p_no = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,projectNO);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            resultSet.getString(1)
        }

    }*/
}
