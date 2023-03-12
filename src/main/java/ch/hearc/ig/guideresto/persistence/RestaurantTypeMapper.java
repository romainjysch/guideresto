package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantTypeMapper {

    // Trouver un type par son nom :
    public RestaurantType findByName(Connection cnn, String restaurantTypeName) throws SQLException {
        try(PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, LIBELLE, DESCRIPTION FROM TYPES_GASTRONOMIQUES WHERE LIBELLE = ?")) {
            statement.setString(1, restaurantTypeName);
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
