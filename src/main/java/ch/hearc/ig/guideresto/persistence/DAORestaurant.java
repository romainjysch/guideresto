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

    private final DAOCity daoCity;
    private final DAORestaurantType daoRestaurantType;
    private final DAOBasicEvaluation daoBasicEvaluation;
    private final DAOCompleteEvaluation daoCompleteEvaluation;
    private final DAOGrade daoGrade;

    public DAORestaurant(DAOCity daoCity, DAORestaurantType daoRestaurantType, DAOBasicEvaluation daoBasicEvaluation, DAOCompleteEvaluation daoCompleteEvaluation, DAOGrade daoGrade) {
        this.daoCity = daoCity;
        this.daoRestaurantType = daoRestaurantType;
        this.daoBasicEvaluation = daoBasicEvaluation;
        this.daoCompleteEvaluation = daoCompleteEvaluation;
        this.daoGrade = daoGrade;
    }

    public Set<Restaurant> findAll() {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement statement = cnn.prepareStatement("SELECT NUMERO, NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL FROM RESTAURANTS")) {
            try(ResultSet resultSet = statement.executeQuery()) {
                Set<Restaurant> restaurants = new HashSet<>();
                while (resultSet.next()) {
                    Restaurant restaurant = new Restaurant(
                            resultSet.getInt("numero"),
                            resultSet.getString("nom"),
                            resultSet.getString("description"),
                            resultSet.getString("site_web"),
                            resultSet.getString("adresse"),
                            daoCity.findByNumero(resultSet.getInt("FK_VILL")),
                            daoRestaurantType.findByNumero(resultSet.getInt("FK_TYPE")));
                    restaurant.getEvaluations().addAll(daoBasicEvaluation.findByNumeroRestaurant(restaurant));
                    restaurant.getEvaluations().addAll(daoCompleteEvaluation.findByNumeroRestaurant(restaurant));
                    restaurants.add(restaurant);
                }
                return restaurants;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(Restaurant restaurant) {
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
            try(ResultSet rs = pStmt.getReturnResultSet()){
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Restaurant restaurant) {
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

    public void delete(Restaurant restaurant) {
        for (Evaluation eval : restaurant.getEvaluations()) {
            daoGrade.delete(eval.getId());
        }
        daoCompleteEvaluation.delete(restaurant);
        daoBasicEvaluation.delete(restaurant);
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
