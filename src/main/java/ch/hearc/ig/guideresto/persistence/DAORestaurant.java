package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DAORestaurant {

    // Trouver tous les restaurants :
    public static Set<Restaurant> findAll() {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL FROM RESTAURANTS")) {
            ResultSet resultSet = statement.executeQuery();
            Set<Restaurant> restaurants = new HashSet<>();
            while(resultSet.next()) {
                restaurants.add(new Restaurant(
                        resultSet.getInt("numero"),
                        resultSet.getString("nom"),
                        resultSet.getString("description"),
                        resultSet.getString("site_web"),
                        resultSet.getString("adresse"),
                        DAOCity.findByNumero(resultSet.getInt("FK_VILL")),
                        DAORestaurantType.findByNumero(resultSet.getInt("FK_TYPE"))));
            }
            return restaurants;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Ajouter un nouveau restaurant :
    public static void insert(Restaurant restaurant) {
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement("INSERT INTO RESTAURANTS (NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL) VALUES (?, ?, ?, ?, ?, ?)")) {
            pStmt.setString(1, restaurant.getName());
            pStmt.setString(2, restaurant.getStreet());
            pStmt.setString(3, restaurant.getDescription());
            pStmt.setString(4, restaurant.getWebsite());
            pStmt.setInt(5, restaurant.getType().getId());
            pStmt.setInt(6, restaurant.getAddress().getCity().getId());
            pStmt.executeUpdate();
            cnn.commit();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
