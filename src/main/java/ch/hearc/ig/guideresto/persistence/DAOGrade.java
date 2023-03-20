package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.CompleteEvaluation;
import ch.hearc.ig.guideresto.business.Evaluation;
import ch.hearc.ig.guideresto.business.Grade;
import ch.hearc.ig.guideresto.business.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAOGrade {

    private static final String SELECT_BY_NUMEROEVAL = "SELECT NUMERO, NOTE, FK_COMM, FK_CRIT FROM NOTES WHERE FK_COMM = ?";
    private static final String INSERT_INTO_NOTES = "INSERT INTO NOTES (NOTE, FK_COMM, FK_CRIT) VALUES (?, ?, ?)";

    public static Set<Grade> findByNumeroEvaluation(CompleteEvaluation eval) {
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement(SELECT_BY_NUMEROEVAL)) {
            pStmt.setInt(1, eval.getId());
            ResultSet resultSet = pStmt.executeQuery();
            Set<Grade> grades = new HashSet<>();
            while (resultSet.next()) {
                Grade grade = new Grade(
                        resultSet.getInt("NUMERO"),
                        resultSet.getInt("NOTE"),
                        eval,
                        DAOEvaluationCriteria.findByNumero(resultSet.getInt("FK_CRIT"))
                );
                grades.add(grade);
            }
            resultSet.close();
            return grades;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insert(Grade grade) {
        try (Connection cnn = DBOracleConnection.openConnection();
             PreparedStatement pStmt = cnn.prepareStatement(INSERT_INTO_NOTES)) {
            pStmt.setInt(1, grade.getGrade());
            pStmt.setInt(2, grade.getEvaluation().getId());
            pStmt.setInt(3, grade.getCriteria().getId());
            pStmt.executeUpdate();
            cnn.commit();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
