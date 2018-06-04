package controllers;

import beans.Status;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class StatusController {

    private int idStatus;
    private String status_type;

    public StatusController() {
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

    public List<Status> sortRequestTypes(List<Status> statuses, int idStatus) {
        List<Status> result = new ArrayList();
        for (int i = 0; i < statuses.size(); i++) {
            if (idStatus == statuses.get(i).getIdStatus()) {
                result.add(statuses.get(i));
            }
        }
        for (int i = 0; i < statuses.size(); i++) {
            if (idStatus != statuses.get(i).getIdStatus()) {
                result.add(statuses.get(i));
            }
        }
        return result;
    }

    public List<Status> takeAllForUser(int idStatus) {
        List<Status> result;
        result = sortRequestTypes(allStatus, idStatus);
        return result;
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

    public void clear() {
        status_type = null;
    }

    public String takeStatusNameById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from status where idStatus=" + id);
            while (rs.next()) {
                status_type = rs.getString("status_type");
            }
            return status_type;
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // INSERT STATUS
    public String insertStatus() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into status (status_type) values (?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, status_type);
            ps.executeUpdate();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }

        return "admin?faces-redirect=true";
    }

    // DELETE TICKET TYPE
    public void deleteStatus(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from status where idstatus = " + id);
            int idx = 0;
            for (int i = 0; i < allStatus.size(); i++) {
                if (allStatus.get(i).getIdStatus() == id) {
                    idx = i;
                }
            }
            allStatus.remove(idx);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }
    }

    // UPDATE TICKET TYPE
    public void updateStatus() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            for (int i = 0; i < allStatus.size(); i++) {
                String currentStatusType = allStatus.get(i).getStatus_type();
                int currentId = allStatus.get(i).getIdStatus();
                String query = "update status set status_type='" + currentStatusType + "' where idstatus= " + currentId;
                stmt.executeUpdate(query);
            }

            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Statuses updated", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();

        }
    }
}
