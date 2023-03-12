package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.*;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestaurantMapper {

    // Trouver une ville par son nom :
    public List<Restaurant> findByName(Connection cnn, String restaurantName) throws SQLException {
        try(PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, NOM, DESCRIPTION, SITE_WEB FROM RESTAURANTS WHERE NOM = ?")) {
            statement.setString(1, restaurantName);
            ResultSet resultSet = statement.executeQuery();
            List<Restaurant> restaurants = new ArrayList<>();
            while(resultSet.next()) {
                restaurants.add(new Restaurant(null, resultSet.getString("nom"), resultSet.getString("description"), resultSet.getString("site_web"), null, null, null));
            }
            return restaurants;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Ajouter un nouveau restaurant :
    public void insert(Connection cnn, Restaurant restaurant) throws SQLException {
        try (PreparedStatement pStmt = cnn.prepareStatement("INSERT INTO RESTAURANTS (NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL) VALUES (?, ?, ?, ?, ?, ?)")) {
            pStmt.setString(1, restaurant.getName());
            pStmt.setString(2, restaurant.getStreet());
            pStmt.setString(3, restaurant.getDescription());
            pStmt.setString(4, restaurant.getWebsite());
            pStmt.setInt(5, restaurant.getType().getId());
            pStmt.setInt(6, restaurant.getAddress().getCity().getId());
            pStmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
