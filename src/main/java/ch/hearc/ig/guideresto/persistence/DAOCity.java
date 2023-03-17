package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAOCity {

    // Trouver toutes les villes :
    public static Set<City> findAll() {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, CODE_POSTAL, NOM_VILLE FROM VILLES")) {
            ResultSet resultSet = statement.executeQuery();
            Set<City> cities = new HashSet<>();
            while(resultSet.next()) {
                cities.add(new City(resultSet.getInt("NUMERO"), resultSet.getString("CODE_POSTAL"), resultSet.getString("NOM_VILLE")));
            }
            return cities;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Trouver une City par son numéro :
    public static City findByNumero(int cityNumero) {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, CODE_POSTAL, NOM_VILLE FROM VILLES WHERE NUMERO = ?")) {
            statement.setInt(1, cityNumero);
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
