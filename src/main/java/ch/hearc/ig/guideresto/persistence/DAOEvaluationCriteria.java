package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAOEvaluationCriteria {

    private static final String SELECT_ALL = "SELECT NUMERO, NOM, DESCRIPTION FROM CRITERES_EVALUATION";
    private static final String SELECT_BYNUMERO = "SELECT NUMERO, NOM, DESCRIPTION FROM CRITERES_EVALUATION WHERE NUMERO = ?";

    public static Set<EvaluationCriteria> findAll() {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement pStmt = cnn.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = pStmt.executeQuery();
            Set<EvaluationCriteria> evaluationCriterias = new HashSet<>();
            while(resultSet.next()) {
                evaluationCriterias.add(new EvaluationCriteria(
                        resultSet.getInt("NUMERO"),
                        resultSet.getString("NOM"),
                        resultSet.getString("DESCRIPTION")
                ));
            }
            resultSet.close();
            return evaluationCriterias;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static EvaluationCriteria findByNumero(int evaluationCriteriaNumero) {
        try(Connection cnn = DBOracleConnection.openConnection();
            PreparedStatement pStmt = cnn.prepareStatement(SELECT_BYNUMERO)) {
            pStmt.setInt(1, evaluationCriteriaNumero);
            ResultSet resultSet = pStmt.executeQuery();
            EvaluationCriteria evaluationCriteria = null;
            while(resultSet.next()) {
                evaluationCriteria = new EvaluationCriteria(
                        resultSet.getInt("NUMERO"),
                        resultSet.getString("NOM"),
                        resultSet.getString("DESCRIPTION")
                );
            }
            resultSet.close();
            return evaluationCriteria;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
