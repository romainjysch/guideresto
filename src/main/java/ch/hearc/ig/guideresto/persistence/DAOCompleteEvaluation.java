package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.*;
import ch.hearc.ig.guideresto.services.DBTransaction;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DAOCompleteEvaluation {

    private static final String SELECT_BY_NUMERORESTAURANT = "SELECT NUMERO, DATE_EVAL, COMMENTAIRE, NOM_UTILISATEUR, FK_REST FROM COMMENTAIRES WHERE FK_REST = ?";
    private static final String INSERT_INTO_COMMENTAIRES = "INSERT INTO COMMENTAIRES (DATE_EVAL, COMMENTAIRE, NOM_UTILISATEUR, FK_REST) VALUES (?, ?, ?, ?) RETURNING NUMERO INTO ?";

    private final DAOGrade daoGrade;

    public DAOCompleteEvaluation(DAOGrade daoGrade) {
        this.daoGrade = daoGrade;
    }

    public Set<Evaluation> findByNumeroRestaurant(DBTransaction dbTransaction, Restaurant restaurant) {
        try (PreparedStatement pStmt = dbTransaction.getOracleConnection().getCnn().prepareStatement(SELECT_BY_NUMERORESTAURANT)) {
            pStmt.setInt(1, restaurant.getId());
            ResultSet resultSet = pStmt.executeQuery();
            Set<Evaluation> evaluations = new HashSet<>();
            while (resultSet.next()) {
                CompleteEvaluation evaluation = new CompleteEvaluation(
                        resultSet.getInt("NUMERO"), resultSet.getDate("DATE_EVAL").toLocalDate(),
                        restaurant,
                        resultSet.getString("COMMENTAIRE"),
                        resultSet.getString("NOM_UTILISATEUR")
                );
                evaluation.getGrades().addAll(dbTransaction.getDaoFactory().getDaoGrade().findByNumeroEvaluation(dbTransaction, evaluation));
                evaluations.add(evaluation);
            }
            resultSet.close();
            return evaluations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(DBTransaction dbTransaction, CompleteEvaluation eval) {
        try {
            return dbTransaction.functionTransaction(cnn -> {
                try (OraclePreparedStatement pStmt = (OraclePreparedStatement) dbTransaction.getOracleConnection().getCnn().prepareStatement(INSERT_INTO_COMMENTAIRES)) {
                    pStmt.setDate(1, Date.valueOf(eval.getVisitDate()));
                    pStmt.setString(2, eval.getComment());
                    pStmt.setString(3, eval.getUsername());
                    pStmt.setInt(4, eval.getRestaurant().getId());
                    pStmt.registerReturnParameter(5, OracleTypes.NUMBER);
                    pStmt.executeUpdate();
                    ResultSet rs = null;
                    rs = pStmt.getReturnResultSet();
                    while (rs.next()) {
                        return rs.getInt(1);
                    }
                    return 0;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(DBTransaction dbTransaction, Restaurant restaurant) {
        dbTransaction.consumerTransaction(cnn -> {
            try (PreparedStatement pStmt = dbTransaction.getOracleConnection().getCnn().prepareStatement("DELETE FROM COMMENTAIRES WHERE FK_REST = ?")) {
                pStmt.setInt(1, restaurant.getId());
                pStmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
