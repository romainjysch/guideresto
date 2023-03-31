package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.CompleteEvaluation;
import ch.hearc.ig.guideresto.business.Grade;
import ch.hearc.ig.guideresto.services.DBTransaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DAOGrade {

    private static final String SELECT_BY_NUMEROEVAL = "SELECT NUMERO, NOTE, FK_COMM, FK_CRIT FROM NOTES WHERE FK_COMM = ?";
    private static final String INSERT_INTO_NOTES = "INSERT INTO NOTES (NOTE, FK_COMM, FK_CRIT) VALUES (?, ?, ?)";

    private final DAOEvaluationCriteria daoEvaluationCriteria;

    public DAOGrade(DAOEvaluationCriteria daoEvaluationCriteria) {
        this.daoEvaluationCriteria = daoEvaluationCriteria;
    }

    public Set<Grade> findByNumeroEvaluation(DBTransaction dbTransaction, CompleteEvaluation eval) {
        try (PreparedStatement pStmt = dbTransaction.getCnn().prepareStatement(SELECT_BY_NUMEROEVAL)) {
            pStmt.setInt(1, eval.getId());
            ResultSet resultSet = pStmt.executeQuery();
            Set<Grade> grades = new HashSet<>();
            while (resultSet.next()) {
                Grade grade = new Grade(
                        resultSet.getInt("NUMERO"),
                        resultSet.getInt("NOTE"),
                        eval,
                        dbTransaction.getDaoFactory().getDaoEvaluationCriteria().findByNumero(dbTransaction, resultSet.getInt("FK_CRIT"))
                );
                grades.add(grade);
            }
            resultSet.close();
            return grades;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(DBTransaction dbTransaction, Grade grade) {
        dbTransaction.consumerTransaction(cnn -> {
            try (PreparedStatement pStmt = dbTransaction.getCnn().prepareStatement(INSERT_INTO_NOTES)) {
                pStmt.setInt(1, grade.getGrade());
                pStmt.setInt(2, grade.getEvaluation().getId());
                pStmt.setInt(3, grade.getCriteria().getId());
                pStmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void delete(DBTransaction dbTransaction, int evaluationNumero) {
        dbTransaction.consumerTransaction(cnn -> {
            try (PreparedStatement pStmt = dbTransaction.getCnn().prepareStatement("DELETE FROM NOTES WHERE FK_COMM = ?")) {
                pStmt.setInt(1, evaluationNumero);
                pStmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}