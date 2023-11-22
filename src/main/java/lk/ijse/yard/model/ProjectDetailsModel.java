package lk.ijse.yard.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.ProjectDto;
import lk.ijse.yard.dto.Tm.ProjectMaterialRequirementTm;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor

@Data
public class ProjectDetailsModel {

    public boolean AddProjectDetails(ProjectDto dto, ObservableList<ProjectMaterialRequirementTm> obList) throws SQLException {

        boolean AddProject = false;
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isAddProject = addProject(dto);

            if (isAddProject){

                boolean isAddProjectMaterialRequirement = ProjectMaterialRequirementModel.AddProjectMaterialRequirement(dto,obList);

                if (isAddProjectMaterialRequirement){
                    connection.commit(); AddProject = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }

        return AddProject;

    }

    private boolean addProject(ProjectDto dto) throws SQLException {
        int affectedRaw = 0;
        try {
           Connection connection = DbConnection.getInstance().getConnection();
           String sql = "INSERT INTO project VALUES (?,?,?,?)";

           PreparedStatement pstm = connection.prepareStatement(sql);

           pstm.setString(1,dto.getProjectNO());
           pstm.setString(2,dto.getProjectName());
           pstm.setString(3, dto.getLoacation());
           pstm.setDate(4, Date.valueOf(dto.getCompletionDate()));

           affectedRaw = pstm.executeUpdate();

           if (affectedRaw > 0){ return true; } else { return false;}
       } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
       }
       return  affectedRaw > 0;
    }

    public List<ProjectDto> loadAllProjects() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM project";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ProjectDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            var dtoProject = new ProjectDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate()
            );
            dtoList.add(dtoProject);
        }

        return dtoList;

    }

    public ProjectDto findOldProjectDetails(String projectNO) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *  FROM project WHERE p_no = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,projectNO);

        ResultSet resultSet = pstm.executeQuery();

        ProjectDto projectDto = null;

        if (resultSet.next()){
//            System.out.println(resultSet.getString(1)+" "+resultSet.getString(2));
            projectDto = new ProjectDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate()
            );
//            System.out.println(materialDto.getUnit());
        }
        return projectDto;

    }

    public boolean updateAllProjectDetails(ProjectDto projectDto, ObservableList<ProjectMaterialRequirementTm> obList) throws SQLException {

        boolean updateProjectdetails = false;
        Connection connection = null;

        try {
            connection =DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstlyUpdateDetails = updateDetails(projectDto);
            if (firstlyUpdateDetails){
                boolean SecondlyupdateMaterialRequirement = ProjectMaterialRequirementModel.updateRequirement(projectDto,obList);
                if (SecondlyupdateMaterialRequirement){
                    connection.commit();
                    updateProjectdetails = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }

        return updateProjectdetails;
    }

    private boolean updateDetails(ProjectDto projectDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE project SET p_name = ? , location = ? , completion_date = ? WHERE p_no = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,projectDto.getProjectName());
        pstm.setString(2,projectDto.getLoacation());
        pstm.setDate(3,Date.valueOf(projectDto.getCompletionDate()));
        pstm.setString(4, projectDto.getProjectNO());

        int affectedRaw = pstm.executeUpdate();

        return affectedRaw > 0;
    }

    public boolean deleteProject(ProjectDto projectDto) throws SQLException {

        boolean projectDelete = false;
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean firstDeleteFromMRequirements = ProjectMaterialRequirementModel.deleteRequirements(projectDto);
            if (firstDeleteFromMRequirements){

                boolean secondlyDeleteProjectTable = firstlydeleteProject(projectDto);
                if (secondlyDeleteProjectTable){
                    connection.commit(); projectDelete = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }

        return projectDelete;
    }

    private boolean firstlydeleteProject(ProjectDto projectDto) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM project WHERE p_no = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, projectDto.getProjectNO());

        return  pstm.executeUpdate() > 0 ;

    }


}
