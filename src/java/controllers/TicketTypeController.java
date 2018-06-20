package controllers;

import beans.TicketType;
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
public class TicketTypeController {

    private int idticket_type;
    private String ticket_type;
    private boolean renderedTicketTable;
    private List<TicketType> allTicketType;
    private TicketType oneTicketType;

    public boolean isRenderedTicketTable() {
        return renderedTicketTable;
    }

    public void setRenderedTicketTable(boolean renderedTicketTable) {
        this.renderedTicketTable = renderedTicketTable;
    }

    public TicketTypeController() {
    }

    public int getIdticket_type() {
        return idticket_type;
    }

    public void setIdticket_type(int idticket_type) {
        this.idticket_type = idticket_type;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }

    public List<TicketType> getAllTicketType() {
        return allTicketType;
    }

    public void setAllTicketType(List<TicketType> allTicketType) {
        this.allTicketType = allTicketType;
    }

    public TicketType getOneTicketType() {
        return oneTicketType;
    }

    public void setOneTicketType(TicketType oneTicketType) {
        this.oneTicketType = oneTicketType;
    }

    public String getTicketTypeName(Integer idTicketType) {
        for (int i = 0; i < allTicketType.size(); i++) {
            if (allTicketType.get(i).getIdticket_type() == idTicketType) {
                return allTicketType.get(i).getTicket_type();
            }
        }
        return null;
    }

    public void clear() {
        ticket_type = null;
    }

    public String takeAllTicketType() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ticket_type");
            allTicketType = new ArrayList<>();
            while (rs.next()) {
                TicketType ticketType = new TicketType();
                ticketType.setIdticket_type(rs.getInt("idticket_type"));
                ticketType.setTicket_type(rs.getString("ticket_type"));
                allTicketType.add(ticketType);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }



    public String takeTicketTypeById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ticket_type where idticket_type=" + id);
            oneTicketType = new TicketType();
            while (rs.next()) {
                oneTicketType.setIdticket_type(rs.getInt("idticket_type"));
                oneTicketType.setTicket_type(rs.getString("ticket_type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String insertTicketType() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into ticket_type (ticket_type) values (?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, ticket_type);
            ps.executeUpdate();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ticket Type created", null);
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
    public void deleteTicketType(int id) throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from ticket_type where idticket_type = " + id);
            int idx = 0;
            for (int i = 0; i < allTicketType.size(); i++) {
                if (allTicketType.get(i).getIdticket_type() == id) {
                    idx = i;
                }
            }
            allTicketType.remove(idx);
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ticket Type deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            reload();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    // UPDATE TICKET TYPE
    public void updateTicketType() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            for (int i = 0; i < allTicketType.size(); i++) {
                String currentTicketType = allTicketType.get(i).getTicket_type();
                int currentId = allTicketType.get(i).getIdticket_type();
                String query = "update ticket_type set ticket_type='" + currentTicketType + "' where idticket_type= " + currentId;
                stmt.executeUpdate(query);
            }

            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ticket Types updated", null);
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
