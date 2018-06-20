package controllers;

import beans.Operator;
import static db.DB.user;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class Login {
    private int id;
    private String username;
    private String password;
    private Operator loggedInOperator;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPassword() {
        return password;
    }

    public Operator getLoggedInOperator() {
        return loggedInOperator;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoggedInOperator(Operator loggedInOperator) {
        this.loggedInOperator = loggedInOperator;
    }

    public void resetInputs() {
        username = "";
        password = "";
    }

    public String login() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from operators where username = '" + username + "'");
            boolean validInput = false;
            while (rs.next()) {
                if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
                    validInput = true;
                    HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    sesija.setAttribute("username", username);
                    id = rs.getInt("id");
                    sesija.setAttribute("operatorId", id);                   
                    Integer type = rs.getInt("type");
                    sesija.setAttribute("type", type);
                    image = rs.getString("img");
                    sesija.setAttribute("image", image);

                    if (type == 1) {
                        return "admin?faces-redirect=true";
                    } else {
                        return "operator?faces-redirect=true";
                    }
                }
            }
            if (!validInput) {

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username and Password", ""));
                resetInputs();
                return "index";

            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
}
