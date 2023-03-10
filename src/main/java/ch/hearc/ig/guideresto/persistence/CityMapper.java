package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityMapper {

    private static Connection cnn = null;
    private static PreparedStatement pStmt = null;

    // Méthodes de connexion :
    public void openConnection() throws SQLException {
        cnn = new DBOracleConnection().getConnection();
    }

    public void closeConnection() throws SQLException {
        cnn.close();
    }

    // findByName :
    public List<City> findByName(String cityName) throws SQLException {
        openConnection();
        try(PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, CODE_POSTAL, NOM_VILLE FROM VILLES");
            ResultSet resultSet = statement.executeQuery()) {
            List<City> cities = new ArrayList<>();
            while(resultSet.next()) {
                cities.add(new City(resultSet.getInt("NUMERO"), resultSet.getString("CODE_POSTAL"), resultSet.getString("NOM_VILLE")));
            }
            return cities;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
