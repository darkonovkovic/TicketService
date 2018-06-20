package controllers;

import beans.Group;
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
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class GroupController {

    private int idGroup;
    private String groupName;
    private Group oneGroup;
    private List<Group> allGroup;
    private List<Group> allGroupWithoutNoGroup;

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Group getOneGroup() {
        return oneGroup;
    }

    public void setOneGroup(Group oneGroup) {
        this.oneGroup = oneGroup;
    }

    public List<Group> getAllGroup() {
        return allGroup;
    }

    public void setAllGroup(List<Group> allGroup) {
        this.allGroup = allGroup;
    }

    public void clear() {
        groupName = null;
    } 

    public List<Group> getAllGroupWithoutNoGroup() {
        return allGroupWithoutNoGroup;
    }

    public void setAllGroupWithoutNoGroup(List<Group> allGroupWithoutNoGroup) {
        this.allGroupWithoutNoGroup = allGroupWithoutNoGroup;
    }

    public List<Group> takeAllForUser(int idGroup) {
        List<Group> result = new ArrayList();
        for (int i = 0; i < allGroup.size(); i++) {
            if (idGroup == allGroup.get(i).getIdGroup()) {
                result.add(allGroup.get(i));
            }
        }
        for (int i = 0; i < allGroup.size(); i++) {
            if (idGroup != allGroup.get(i).getIdGroup()) {
                result.add(allGroup.get(i));
            }
        }
        return result;
    }

    public String takeAllGroupSubjects() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ticket_service.group");
            allGroup = new ArrayList<>();
            allGroupWithoutNoGroup = new ArrayList<>();
            while (rs.next()) {
                Group group = new Group();
                group.setIdGroup(rs.getInt("idgroup"));
                group.setSubject(rs.getString("subject"));
                if (group.getIdGroup() == 1) {
                    allGroup.add(group);
                } else {
                    allGroup.add(group);
                    allGroupWithoutNoGroup.add(group);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(GroupController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String takeGroupById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from group where idgroup=" + id);
            oneGroup = new Group();
            while (rs.next()) {
                oneGroup.setIdGroup(rs.getInt("idgroup"));
                oneGroup.setSubject(rs.getString("subject"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GroupController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String takeGroupNameById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from ticket_service.group where idgroup=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String grName = "No group";
            while (rs.next()) {
                grName = rs.getString("subject");
            }
            return grName;
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    // INSERT GROUP IN DB
    public String insertGroup() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into ticket_service.group (subject) values (?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, groupName);
            ps.executeUpdate();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Group created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }

        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Integer type = (Integer) sesija.getAttribute("type");

        if (type == 1) {
            return "admin?faces-redirect=true";
        } else {
            return "operator?faces-redirect=true";
        }
    }

    public void closeTicket(int idTicket) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "update tickets set idgroup = 1 where id_ticket = " + idTicket;
            stmt.executeUpdate(query);
            String query1 = "update tickets set fk_idstatus = 3 where id_ticket = " + idTicket; 
            stmt.executeUpdate(query1);
        } catch (SQLException ex) {
            Logger.getLogger(GroupController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    // Close ALL Tickets from Group
    public void closeTicketsFromGroup(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from tickets where idgroup =" + id;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int idTicket = rs.getInt("id_ticket");
                closeTicket(idTicket);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GroupController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    // DELETE GROUP
    public void deleteGroup(int id) throws IOException {
        closeTicketsFromGroup(id);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from ticket_service.group where idgroup = " + id);
            int idx = 0;
            for (int i = 0; i < allGroup.size(); i++) {
                if (allGroup.get(i).getIdGroup() == id) {
                    idx = i;
                }
            }
            allGroup.remove(idx);
            for (int i = 0; i < allGroupWithoutNoGroup.size(); i++) {
                if (allGroupWithoutNoGroup.get(i).getIdGroup() == id) {
                    idx = i;
                }
            }
            allGroupWithoutNoGroup.remove(idx);
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Group deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            reload();
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
