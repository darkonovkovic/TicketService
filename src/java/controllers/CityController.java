package controllers;

import beans.City;
import beans.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CityController {

    private String idcity;
    private String name;

    public String getIdcity() {
        return idcity;
    }

    public void setIdcity(String idcity) {
        this.idcity = idcity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<City> allCities;

    public List<City> getAllCities() {
        return allCities;
    }

    public void setAllCities(List<City> allCities) {
        this.allCities = allCities;
    }

    public List<City> sortCities(List<City> allCities, Integer idCity) {
        List<City> resultCities = new ArrayList();
        for (int i = 0; i < allCities.size(); i++) {
            if (idCity == allCities.get(i).getIdCity()) {
                resultCities.add(allCities.get(i));
            }
        }
        for (int i = 0; i < allCities.size(); i++) {
            if (idCity != allCities.get(i).getIdCity()) {
                resultCities.add(allCities.get(i));
            }
        }
        return resultCities;

    }

    public void takeAllCities() {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from cities");
            allCities = new ArrayList<>();
            while (rs.next()) {
                City city = new City();
                city.setIdCity(rs.getInt("idcity"));
                city.setName(rs.getString("name"));
                allCities.add(city);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<City> takeAllForUser(Integer idCity) {
        List<City> resultCity;
        resultCity = sortCities(allCities, idCity);
        return resultCity;
    }

    private List<City> oneCity;

    public List<City> getOneCity() {
        return oneCity;
    }

    public void setOneCity(List<City> oneCity) {
        this.oneCity = oneCity;
    }

    public String takeCityById(int id) {
        try {
            Connection conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from cities where id = " + id);
            oneCity = new ArrayList<>();
            while (rs.next()) {
                City city = new City();
                city.setIdCity(rs.getInt("idcity"));
                city.setName(rs.getString("name"));
                oneCity.add(city);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
