package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.Evaluation;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.services.DBTransaction;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DAOBasicEvaluation {

    private static final String SELECT_BY_NUMERORESTAURANT = "SELECT NUMERO, APPRECIATION, DATE_EVAL, ADRESSE_IP, FK_REST FROM LIKES WHERE FK_REST = ?";
    private static final String INSERT_INTO_LIKES = "INSERT INTO LIKES (APPRECIATION, DATE_EVAL, ADRESSE_IP, FK_REST) VALUES (?, ?, ?, ?)";

    public Set<Evaluation> findByNumeroRestaurant(DBTransaction dbTransaction, Restaurant restaurant) {
        try(PreparedStatement pStmt = dbTransaction.getOracleConnection().getCnn().prepareStatement(SELECT_BY_NUMERORESTAURANT)) {
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

    public void insert(DBTransaction dbTransaction, BasicEvaluation eval) {
        dbTransaction.consumerTransaction(cnn -> {
            try (PreparedStatement pStmt = dbTransaction.getOracleConnection().getCnn().prepareStatement(INSERT_INTO_LIKES)) {
                pStmt.setString(1, DAOUtils.booleanToString(eval.isLikeRestaurant()));
                pStmt.setDate(2, Date.valueOf(eval.getVisitDate()));
                pStmt.setString(3, eval.getIpAddress());
                pStmt.setInt(4, eval.getRestaurant().getId());
                pStmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void delete(DBTransaction dbTransaction, Restaurant restaurant) {
        dbTransaction.consumerTransaction(cnn -> {
            try (PreparedStatement pStmt = dbTransaction.getOracleConnection().getCnn().prepareStatement("DELETE FROM LIKES WHERE FK_REST = ?")) {
                pStmt.setInt(1, restaurant.getId());
                pStmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
