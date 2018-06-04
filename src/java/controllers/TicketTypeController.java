package controllers;

import beans.TicketType;
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
public class TicketTypeController {

    private int idticket_type;
    private String ticket_type;
    private boolean renderedTicketTable;

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

    private List<TicketType> allTicketType;

    public List<TicketType> getAllTicketType() {
        return allTicketType;
    }

    public void setAllTicketType(List<TicketType> allTicketType) {
        this.allTicketType = allTicketType;
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
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
        }
        return null;
    }

    private List<TicketType> oneTicketType;

    public List<TicketType> getOneTicketType() {
        return oneTicketType;
    }

    public void setOneTicketType(List<TicketType> oneTicketType) {
        this.oneTicketType = oneTicketType;
    }

    public String takeTicketTypeById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ticket_type where idticket_type=" + id);
            oneTicketType = new ArrayList<>();
            while (rs.next()) {
                TicketType ticketType = new TicketType();
                ticketType.setIdticket_type(rs.getInt("idticket_type"));
                ticketType.setTicket_type(rs.getString("ticket_type"));
                oneTicketType.add(ticketType);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String insertTicketType() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
        }

        return "admin?faces-redirect=true";
    }

    // DELETE TICKET TYPE
    public void deleteTicketType(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from ticket_type where idticket_type = " + id);
            int idx = 0;
            for (int i = 0; i < allTicketType.size(); i++) {
                if (allTicketType.get(i).getIdticket_type() == id) {
                    idx = i;
                }
            }
            allTicketType.remove(idx);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ticket Type deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }
    }

    // UPDATE TICKET TYPE
    public void updateTicketType() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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

        }
    }

}
