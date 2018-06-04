package controllers;

import beans.Status;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class StatusControler {

    private int idStatus;
    private String status_type;

    public StatusControler() {
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    private List<Status> allStatus;

    public List<Status> getAllStatus() {
        return allStatus;
    }

    public void setAllStatus(List<Status> allStatus) {
        this.allStatus = allStatus;
    }

    public String takeAllStatus() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from status");
            allStatus = new ArrayList<>();
            while (rs.next()) {
                Status status = new Status();
                status.setIdStatus(rs.getInt("idStatus"));
                status.setStatus_type(rs.getString("status_type"));
                allStatus.add(status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Status> oneStatus;

    public List<Status> getOneStatus() {
        return oneStatus;
    }

    public void setOneStatus(List<Status> oneStatus) {
        this.oneStatus = oneStatus;
    }

    public String takeStatusById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from status where idStatus=" + id);
            oneStatus = new ArrayList<>();
            while (rs.next()) {
                Status status = new Status();
                status.setIdStatus(rs.getInt("idStatus"));
                status.setStatus_type(rs.getString("status_type"));
                oneStatus.add(status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
