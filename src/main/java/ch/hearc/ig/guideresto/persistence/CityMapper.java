package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityMapper {

    // Trouver une ville par son nom :
    public City findByName(Connection cnn, String cityName) throws SQLException {
        try(PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, CODE_POSTAL, NOM_VILLE FROM VILLES WHERE NOM_VILLE = ?")) {
            statement.setString(1, cityName);
            ResultSet resultSet = statement.executeQuery();
            City city = null;
            while (resultSet.next()) {
                city = new City(resultSet.getInt("NUMERO"), resultSet.getString("CODE_POSTAL"), resultSet.getString("NOM_VILLE"));
            }
            return city;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Ajouter une nouvelle ville :
    public void insert(Connection cnn, City city) throws SQLException {
        try (PreparedStatement pStmt = cnn.prepareStatement("INSERT INTO VILLES (CODE_POSTAL, NOM_VILLE) VALUES (?, ?)")) {
            pStmt.setString(1, city.getZipCode());
            pStmt.setString(2, city.getCityName());
            pStmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
