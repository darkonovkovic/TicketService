package controllers;

import beans.Status;
import java.io.IOException;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class StatusController {

    private int idStatus;
    private String status_type;
    private List<Status> allStatus;
    private Status oneStatus;

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

    public Status getOneStatus() {
        return oneStatus;
    }

    public void setOneStatus(Status oneStatus) {
        this.oneStatus = oneStatus;
    }

    public List<Status> getAllStatus() {
        return allStatus;
    }

    public void setAllStatus(List<Status> allStatus) {
        this.allStatus = allStatus;
    }

    public List<Status> takeAllForUser(int idStatus) {
        List<Status> result = new ArrayList();
        for (int i = 0; i < allStatus.size(); i++) {
            if (idStatus == allStatus.get(i).getIdStatus()) {
                result.add(allStatus.get(i));
            }
        }
        for (int i = 0; i < allStatus.size(); i++) {
            if (idStatus != allStatus.get(i).getIdStatus()) {
                result.add(allStatus.get(i));
            }
        }
        return result;
    }

    public String takeAllStatus() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }


    public String takeStatusById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from status where idStatus=" + id);
            oneStatus = new Status();
            while (rs.next()) {
                oneStatus.setIdStatus(rs.getInt("idStatus"));
                oneStatus.setStatus_type(rs.getString("status_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public void clear() {
        status_type = null;
    }

    public String takeStatusNameById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from status where idStatus=" + id);
            while (rs.next()) {
                status_type = rs.getString("status_type");
            }
            return status_type;
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    // INSERT STATUS
    public String insertStatus() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }

        return "admin?faces-redirect=true";
    }

    // DELETE TICKET TYPE
    public void deleteStatus(int id) throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from status where idstatus = " + id);
            int idx = 0;
            for (int i = 0; i < allStatus.size(); i++) {
                if (allStatus.get(i).getIdStatus() == id) {
                    idx = i;
                }
            }
            allStatus.remove(idx);
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            reload();
            clear();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    // UPDATE TICKET TYPE
    public void updateStatus() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    //    RELOADS PAGE
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
