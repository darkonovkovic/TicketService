package controllers;

import beans.Group;
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
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class GroupController {

    private int idGroup;
    private String groupName;

    public GroupController() {
    }

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

    private List<Group> allGroup;

    public List<Group> getAllGroup() {
        return allGroup;
    }

    public void setAllGroup(List<Group> allGroup) {
        this.allGroup = allGroup;
    }

    public void clear() {
        groupName = null;
    }

    private List<Group> allGroupWithoutNoGroup;

    public List<Group> getAllGroupWithoutNoGroup() {
        return allGroupWithoutNoGroup;
    }

    public void setAllGroupWithoutNoGroup(List<Group> allGroupWithoutNoGroup) {
        this.allGroupWithoutNoGroup = allGroupWithoutNoGroup;
    }

    public List<Group> sortRequestTypes(List<Group> groups, Integer idGroup) {
        List<Group> result = new ArrayList();
        for (int i = 0; i < groups.size(); i++) {
            if (idGroup == groups.get(i).getIdGroup()) {
                result.add(groups.get(i));
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            if (idGroup != groups.get(i).getIdGroup()) {
                result.add(groups.get(i));
            }
        }
        return result;
    }

    public List<Group> takeAllForUser(int idGroup) {
        List<Group> result;
        result = sortRequestTypes(allGroup, idGroup);
        return result;
    }

    public String takeAllGroupSubjects() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
        }
        return null;
    }

    private List<Group> oneGroup;

    public List<Group> getOneGroup() {
        return oneGroup;
    }

    public void setOneGroup(List<Group> oneGroup) {
        this.oneGroup = oneGroup;
    }

    public String takeGroupById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from group where idgroup=" + id);
            oneGroup = new ArrayList<>();
            while (rs.next()) {
                Group group = new Group();
                group.setIdGroup(rs.getInt("idgroup"));
                group.setSubject(rs.getString("subject"));
                oneGroup.add(group);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GroupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String takeGroupNameById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from ticket_service.group where idgroup=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                groupName = rs.getString("subject");
            }
            return groupName;
        } catch (SQLException ex) {
            System.err.println("Error");
        }
        return null;
    }

    // INSERT GROUP IN DB
    public String insertGroup() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into ticket_service.group (subject) values (?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, groupName);
            ps.executeUpdate();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Group created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }

        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Integer type = (Integer) sesija.getAttribute("type");

        if (type == 1) {
            return "admin?faces-redirect=true";
        } else {
            return "operator?faces-redirect=true";
        }
    }

    // DELETE GROUP
    public void deleteGroup(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
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
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Group deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }
    }
}
