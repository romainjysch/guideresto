package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.Evaluation;
import ch.hearc.ig.guideresto.business.Restaurant;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DAOBasicEvaluation {

    private static final String SELECT_BY_NUMERORESTAURANT = "SELECT NUMERO, APPRECIATION, DATE_EVAL, ADRESSE_IP, FK_REST FROM LIKES WHERE FK_REST = ?";
    private static final String INSERT_INTO_LIKES = "INSERT INTO LIKES (APPRECIATION, DATE_EVAL, ADRESSE_IP, FK_REST) VALUES (?, ?, ?, ?)";

    public static Set<Evaluation> findByNumeroRestaurant(Restaurant restaurant) {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement pStmt = cnn.prepareStatement(SELECT_BY_NUMERORESTAURANT)) {
            pStmt.setInt(1, restaurant.getId());
            ResultSet resultSet = pStmt.executeQuery();
            Set<Evaluation> evaluations = new HashSet<>();
            while(resultSet.next()) {
                evaluations.add(new BasicEvaluation(
                        resultSet.getInt("NUMERO"),
                        resultSet.getDate("DATE_EVAL").toLocalDate(),
                        restaurant,
                        DAOUtils.charToBoolean(resultSet.getString("APPRECIATION")),
                        resultSet.getString("ADRESSE_IP")));
            }
            resultSet.close();
            return evaluations;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insert(BasicEvaluation eval) {
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement(INSERT_INTO_LIKES)) {
            pStmt.setString(1, DAOUtils.booleanToString(eval.isLikeRestaurant()));
            pStmt.setDate(2, Date.valueOf(eval.getVisitDate()));
            pStmt.setString(3, eval.getIpAddress());
            pStmt.setInt(4, eval.getRestaurant().getId());
            pStmt.executeUpdate();
            cnn.commit();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(Restaurant restaurant) {
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement("DELETE FROM LIKES WHERE FK_REST = ?")) {
            pStmt.setInt(1, restaurant.getId());
            pStmt.executeUpdate();
            cnn.commit();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
