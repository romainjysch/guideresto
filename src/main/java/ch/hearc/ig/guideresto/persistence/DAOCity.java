package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAOCity {

    public Set<City> findAll(Connection connection) {
        try(PreparedStatement statement = connection.prepareStatement("SELECT NUMERO, CODE_POSTAL, NOM_VILLE FROM VILLES");
            ResultSet resultSet = statement.executeQuery()) {
            Set<City> cities = new HashSet<>();
            while(resultSet.next()) {
                cities.add(new City(resultSet.getInt("NUMERO"), resultSet.getString("CODE_POSTAL"), resultSet.getString("NOM_VILLE")));
            }
            return cities;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public City findByNumero(Connection connection, int cityNumero) {
        try(PreparedStatement statement = connection.prepareStatement("SELECT NUMERO, CODE_POSTAL, NOM_VILLE FROM VILLES WHERE NUMERO = ?")) {
            statement.setInt(1, cityNumero);
            try(ResultSet resultSet = statement.executeQuery()) {
                City city = null;
                while (resultSet.next()) {
                    city = new City(resultSet.getInt("NUMERO"), resultSet.getString("CODE_POSTAL"),
                        resultSet.getString("NOM_VILLE"));
                }
                return city;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int findByZipAndName(Connection connection, City city) {
        try(PreparedStatement statement = connection.prepareStatement("SELECT NUMERO FROM VILLES WHERE CODE_POSTAL = ? AND NOM_VILLE = ?")) {
            statement.setString(1, city.getZipCode());
            statement.setString(2, city.getCityName());
            try(ResultSet resultSet = statement.executeQuery()) {
                int cityId = 0;
                while (resultSet.next()) {
                    cityId = resultSet.getInt("NUMERO");
                }
                return cityId;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Connection connection, City city) {
        try (PreparedStatement pStmt = connection.prepareStatement("INSERT INTO VILLES (CODE_POSTAL, NOM_VILLE) VALUES (?, ?)")) {
            pStmt.setString(1, city.getZipCode());
            pStmt.setString(2, city.getCityName());
            pStmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
