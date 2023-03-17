package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAORestaurantType {

    // Trouver tous les types de restaurant :
    public static Set<RestaurantType> findAll() {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, LIBELLE, DESCRIPTION FROM TYPES_GASTRONOMIQUES")) {
            ResultSet resultSet = statement.executeQuery();
            Set<RestaurantType> restaurantTypes = new HashSet<>();
            while(resultSet.next()) {
                restaurantTypes.add(new RestaurantType(resultSet.getInt("NUMERO"),
                        resultSet.getString("LIBELLE"),
                        resultSet.getString("DESCRIPTION")));
            }
            return restaurantTypes;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Trouver un RestaurantType par son numéro :
    public static RestaurantType findByNumero(int restaurantTypeNumero) {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, LIBELLE, DESCRIPTION FROM TYPES_GASTRONOMIQUES WHERE NUMERO = ?")) {
            statement.setInt(1, restaurantTypeNumero);
            ResultSet resultSet = statement.executeQuery();
            RestaurantType restaurantType = null;
            while (resultSet.next()) {
                restaurantType = new RestaurantType(resultSet.getInt("numero"), resultSet.getString("libelle"), resultSet.getString("description"));
            }
            return restaurantType;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
