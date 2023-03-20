package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Evaluation;
import ch.hearc.ig.guideresto.business.Restaurant;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAORestaurant {

    public static Set<Restaurant> findAll() {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL FROM RESTAURANTS")) {
            ResultSet resultSet = statement.executeQuery();
            Set<Restaurant> restaurants = new HashSet<>();
            while(resultSet.next()) {
                Restaurant restaurant = new Restaurant(
                        resultSet.getInt("numero"),
                        resultSet.getString("nom"),
                        resultSet.getString("description"),
                        resultSet.getString("site_web"),
                        resultSet.getString("adresse"),
                        DAOCity.findByNumero(resultSet.getInt("FK_VILL")),
                        DAORestaurantType.findByNumero(resultSet.getInt("FK_TYPE")));
                restaurant.getEvaluations().addAll(DAOBasicEvaluation.findByNumeroRestaurant(restaurant));
                restaurant.getEvaluations().addAll(DAOCompleteEvaluation.findByNumeroRestaurant(restaurant));
                restaurants.add(restaurant);
            }
            resultSet.close();
            return restaurants;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int insert(Restaurant restaurant) {
        try (Connection cnn = DBOracleConnection.openConnection();
             OraclePreparedStatement pStmt = (OraclePreparedStatement) cnn.prepareStatement("INSERT INTO RESTAURANTS (NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL) VALUES (?, ?, ?, ?, ?, ?) RETURNING NUMERO INTO ?")) {
            pStmt.setString(1, restaurant.getName());
            pStmt.setString(2, restaurant.getStreet());
            pStmt.setString(3, restaurant.getDescription());
            pStmt.setString(4, restaurant.getWebsite());
            pStmt.setInt(5, restaurant.getType().getId());
            pStmt.setInt(6, restaurant.getAddress().getCity().getId());
            pStmt.registerReturnParameter(7, OracleTypes.NUMBER);
            pStmt.executeUpdate();
            cnn.commit();
            ResultSet rs = null;
            rs = pStmt.getReturnResultSet();
            while (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(Restaurant restaurant) {
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement("UPDATE RESTAURANTS SET NOM = ?, ADRESSE = ?, DESCRIPTION = ?, SITE_WEB = ?, FK_TYPE = ?, FK_VILL = ? WHERE NUMERO = ?")) {
            pStmt.setString(1, restaurant.getName());
            pStmt.setString(2, restaurant.getStreet());
            pStmt.setString(3, restaurant.getDescription());
            pStmt.setString(4, restaurant.getWebsite());
            pStmt.setInt(5, restaurant.getType().getId());
            pStmt.setInt(6, restaurant.getAddress().getCity().getId());
            pStmt.setInt(7, restaurant.getId());
            pStmt.executeUpdate();
            cnn.commit();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(Restaurant restaurant) {
        for (Evaluation eval : restaurant.getEvaluations()) {
            DAOGrade.delete(eval.getId());
        }
        DAOCompleteEvaluation.delete(restaurant);
        DAOBasicEvaluation.delete(restaurant);
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement("DELETE FROM RESTAURANTS WHERE NUMERO = ?")) {
            pStmt.setInt(1, restaurant.getId());
            pStmt.executeUpdate();
            cnn.commit();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
