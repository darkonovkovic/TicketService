package controllers;

import beans.RequestType;
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
public class RequestTypeController {

    private int idrequest_type;
    private String request_type;
    private String ticketRequestName;
    private List<RequestType> allRequestType;
    private RequestType oneRequestType;

    public int getIdrequest_type() {
        return idrequest_type;
    }

    public void setIdrequest_type(int idrequest_type) {
        this.idrequest_type = idrequest_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public String getTicketRequestName() {
        return ticketRequestName;
    }

    public void setTicketRequestName(String ticketRequestName) {
        this.ticketRequestName = ticketRequestName;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }   

    public List<RequestType> getAllRequestType() {
        return allRequestType;
    }

    public void setAllRequestType(List<RequestType> allRequestType) {
        this.allRequestType = allRequestType;
    }

    public RequestType getOneRequestType() {
        return oneRequestType;
    }

    public void setOneRequestType(RequestType oneRequestType) {
        this.oneRequestType = oneRequestType;
    }

    public List<RequestType> takeAllForUser(int idRequestType) {
        List<RequestType> resultRequestTypes = new ArrayList();
        for (int i = 0; i < allRequestType.size(); i++) {
            if (idRequestType == allRequestType.get(i).getIdrequest_type()) {
                resultRequestTypes.add(allRequestType.get(i));
            }
        }
        for (int i = 0; i < allRequestType.size(); i++) {
            if (idRequestType != allRequestType.get(i).getIdrequest_type()) {
                resultRequestTypes.add(allRequestType.get(i));
            }
        }
        return resultRequestTypes;
    }

    public String takeAllRequestType() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from request_type");
            allRequestType = new ArrayList<>();
            while (rs.next()) {
                RequestType requestType = new RequestType();
                requestType.setIdrequest_type(rs.getInt("idrequest_type"));
                requestType.setRequest_type(rs.getString("request_type"));
                allRequestType.add(requestType);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }



    public String takeRequestTypeById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from request_type where idrequest_type=" + id);
            oneRequestType = new RequestType();
            while (rs.next()) {
                oneRequestType.setIdrequest_type(rs.getInt("idrequest_type"));
                oneRequestType.setRequest_type(rs.getString("request_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public void clear() {
        request_type = null;
    }

    public String takeRequestNameById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from request_type where idrequest_type=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ticketRequestName = rs.getString("request_type");
            }
            return ticketRequestName;
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    // INSERT STATUS
    public String insertRequestType() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into request_type (request_type) values (?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, request_type);
            ps.executeUpdate();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Request type created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            reload();
            clear();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }

        return "admin?faces-redirect=true";
    }

    // DELETE TICKET TYPE
    public void deleteRequestType(int id) throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from request_type where idrequest_type = " + id);
            int idx = 0;
            for (int i = 0; i < allRequestType.size(); i++) {
                if (allRequestType.get(i).getIdrequest_type() == id) {
                    idx = i;
                }
            }
            allRequestType.remove(idx);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Request Type deleted", null);
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
    public void updateRequestType() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            for (int i = 0; i < allRequestType.size(); i++) {
                String currentReqType = allRequestType.get(i).getRequest_type();
                int currentId = allRequestType.get(i).getIdrequest_type();
                String query = "update request_type set request_type='" + currentReqType + "' where idrequest_type= " + currentId;
                stmt.executeUpdate(query);
            }

            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Request types updated", null);
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
