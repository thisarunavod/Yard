package lk.ijse.yard.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.yard.db.DbConnection;
import lk.ijse.yard.dto.MaterialDto;
import lk.ijse.yard.dto.Tm.MaterialTm;
import lk.ijse.yard.model.MaterialModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class MaterialListFormController {

    private final MaterialModel materialModel = new MaterialModel();

    @FXML
    private TableColumn<?, ?> colMaterialID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnit;

    @FXML
    private TableView<MaterialTm> tableViewMaterialList;

    @FXML
    void initialize(){
        loadAllMaterials();
        setCellValueFactory();

    }
    private void setCellValueFactory() {
        colMaterialID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("MaterialName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyAvailable"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    private void loadAllMaterials() {

        try {
            List <MaterialDto> dtoList = materialModel.loadAllMaterials();
            ObservableList<MaterialTm> obList = FXCollections.observableArrayList();

            for (int i = 0; i < dtoList.size(); i++) {

                var tm = new MaterialTm(
                        dtoList.get(i).getMaterialID(),
                        dtoList.get(i).getMaterialName(),
                        dtoList.get(i).getType(),
                        dtoList.get(i).getQtyAvailable(),
                        dtoList.get(i).getUnit()
                        );

                obList.add(tm);
            }
            tableViewMaterialList.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnOnActionReportMaterialList(ActionEvent event) throws JRException, SQLException {

        InputStream resourceAsStream = getClass().getResourceAsStream("/lk.ijse.yard/Reports/MaterialList.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport compileReport = JasperCompileManager.compileReport(load);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        compileReport, //compiled report
                        null,
                        DbConnection.getInstance().getConnection() //database connection
                );
        JasperViewer.viewReport(jasperPrint,false);

    }

}
