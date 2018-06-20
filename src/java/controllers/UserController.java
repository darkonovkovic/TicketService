package controllers;

import beans.User;
import beans.City;
import controllers.CityController;
import static db.DB.user;
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
public class UserController {

    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private Integer idCity;
    private String cityName;
    private boolean insertFlag;
    private boolean searchFlag;
    private boolean renderUserTable;

    private User currentUser;

//    GETTERS AND SETTERS
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isRenderUserTable() {
        return renderUserTable;
    }

    public void setRenderUserTable(boolean renderUserTable) {
        this.renderUserTable = renderUserTable;
    }

    public boolean isInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(boolean insertFlag) {
        this.insertFlag = false;
    }

    public boolean isSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIdCity() {
        return idCity;
    }

    public void setIdCity(Integer idCity) {
        this.idCity = idCity;
    }

    public void addCurrentUser(User user) {
        currentUser = user;
    }

    List<User> allUsers;

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

//    RETURN CITYNAME FOR GIVEN CITYID
    public String takeCityNameById(Integer id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from cities where idcity=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                cityName = rs.getString("name");
            }
            return cityName;
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }
        return null;
    }

    // CLEAR USER BEAN
    public void clear() {
        id = null;
        firstName = null;
        lastName = null;
        phoneNumber = null;
        email = null;
        address = null;
        idCity = null;
        currentUser = null;
    }

//    INSERT USER IN DATABASE
    public String insertUser() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into users (first_name, last_name, phone_number, email, address, id_city) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phoneNumber);
            ps.setString(4, email);
            ps.setString(5, address);
            ps.setInt(6, idCity);
            ps.executeUpdate();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lastCreatedUserTable();
            clear();
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Integer type = (Integer) sesija.getAttribute("type");
        if (type == 1) {
            return "admin?faces-redirect=true";
        } else {
            return "operator?faces-redirect=true";
        }

    }

//    DELETE USER FROM DATABASE
    public void deleteUser(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from users where id_user = " + id);
            int idx = 0;
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getId() == id) {
                    idx = i;
                }
            }
            allUsers.remove(idx);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }
    }

//    SEARCH USERS IN DATABASE
    public List<User> searchUser() {
        String idUserQuery = (id == null) ? "'%'" : Integer.toString(id);
        String firstNameQuery = ("".equals(firstName) || " ".equals(firstName)) ? "'%'" : "'%" + firstName + "%'";
        String lastNameQuery = ("".equals(lastName) || " ".equals(lastName)) ? "'%'" : "'%" + lastName + "%'";
        String emailQuery = ("".equals(email) || " ".equals(email)) ? "'%'" : "'%" + email + "%'";
        String idCityQuery = Integer.toString(idCity);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from users where id_user like " + idUserQuery;
            query += " and first_name like " + firstNameQuery;
            query += " and last_name like " + lastNameQuery;
            query += " and email like " + emailQuery;
            query += " and id_city=" + idCityQuery;
            ResultSet rs = stmt.executeQuery(query);
            allUsers = new ArrayList<User>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id_user"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setIdCity(rs.getInt("id_city"));
                allUsers.add(user);
                renderUserTable = true;
            }
            if (allUsers.size() == 0) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getFlash().setKeepMessages(true);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User not found", null);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }
        return null;
    }

//    SAVE USER DATA IN CONTROLLER
    public String saveUser(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        address = user.getAddress();
        idCity = user.getIdCity();
        currentUser = user;
        return "operator";
    }

//    FINDS USER IN ALLUSERS LIST BY ID
    public User findUser(int id) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getId() == id) {
                return allUsers.get(i);
            }
        }
        return null;
    }

//    UPDATES USER IN DATABASE
    public void updateUser() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "update users set first_name='" + firstName + "', ";
            query += "last_name='" + lastName + "', ";
            query += "phone_number='" + phoneNumber + "', ";
            query += "email='" + email + "', ";
            query += "address='" + address + "', ";
            query += "id_city='" + idCity + "' ";
            query += "where id_user = " + id;
            stmt.executeUpdate(query);

            User updatedUser = findUser(id);
            updatedUser.setFirstName(firstName);
            updatedUser.setLastName(lastName);
            updatedUser.setPhoneNumber(phoneNumber);
            updatedUser.setEmail(email);
            updatedUser.setIdCity(idCity);
            updatedUser.setAddress(address);

        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }
    }

//    RELOADS PAGE
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

//    OPENS TABLE
    public void openTable() throws IOException {
        renderUserTable = true;
        reload();
    }

//    CLOSES USER TABLE
    public void closeTable() {
        renderUserTable = false;
    }

//    LAST CREATED USER
    public void lastCreatedUserTable() throws IOException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from users order by id_user desc limit 1";
            ResultSet rs = stmt.executeQuery(query);
            allUsers = new ArrayList<User>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id_user"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setIdCity(rs.getInt("id_city"));
                allUsers.add(user);
                renderUserTable = true;
                reload();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }
    }
}
