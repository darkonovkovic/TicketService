package controllers;

import beans.Operator;
import beans.RequestType;
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
public class OperatorController {

    private int id;
    private String username;
    private String password;
    private int type;
    private String image;
    private List<Operator> allOperators;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    

    public List<Operator> getAllOperators() {
        return allOperators;
    }

    public void setAllOperators(List<Operator> allOperators) {
        this.allOperators = allOperators;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String takeAllOperators() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from operators");
            allOperators = new ArrayList<>();
            while (rs.next()) {
                Operator operator = new Operator();
                operator.setId(rs.getInt("id"));
                operator.setType(rs.getInt("type"));
                operator.setUsername(rs.getString("username"));
                operator.setPassword(rs.getString("password"));
                operator.setImage(rs.getString("img"));
                allOperators.add(operator);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getOperatorById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from operators where id=" + id;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                username = rs.getString("username");
            }
            return username;

        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void clear() {
        username = null;
        password = null;
    }

    // INSERT STATUS
    public String insertOperator() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into operators (username, password, type, img) values (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, type);
            ps.setString(4, image);
            ps.executeUpdate();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Operator created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }

        return "admin?faces-redirect=true";
    }

    // DELETE TICKET TYPE
    public void deleteOperator(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from operators where id = " + id);
            int idx = 0;
            for (int i = 0; i < allOperators.size(); i++) {
                if (allOperators.get(i).getId() == id) {
                    idx = i;
                }
            }
            allOperators.remove(idx);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Operator deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }
    }
//

    public String convertTypeToString(int type) {
        if (type == 1) {
            return "Admin";
        } else {
            return "Operator";
        }
    }

    // UPDATE TICKET TYPE
    public void updateOperator() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            for (int i = 0; i < allOperators.size(); i++) {
                String currentUsername = allOperators.get(i).getUsername();
                String currentPassword = allOperators.get(i).getPassword();
                int currentType = allOperators.get(i).getType();
                int currentId = allOperators.get(i).getId();
                String currentUrl = allOperators.get(i).getImage();
                String query = "update operators set password='" + currentUsername + "', password='" + currentPassword + "', type=" + currentType + ", img='" + currentUrl + "' where id= " + currentId;
                stmt.executeUpdate(query);
            }

            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Operators updated", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
        }
    }
}
