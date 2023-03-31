package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Evaluation;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.services.DBTransaction;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

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

    public Set<Restaurant> findAll(DBTransaction dbTransaction) {
        try(PreparedStatement statement = dbTransaction.getCnn().prepareStatement("SELECT NUMERO, NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL FROM RESTAURANTS")) {
            try(ResultSet resultSet = statement.executeQuery()) {
                Set<Restaurant> restaurants = new HashSet<>();
                while (resultSet.next()) {
                    Restaurant restaurant = new Restaurant(
                            resultSet.getInt("numero"),
                            resultSet.getString("nom"),
                            resultSet.getString("description"),
                            resultSet.getString("site_web"),
                            resultSet.getString("adresse"),
                            dbTransaction.getDaoFactory().getDaoCity().findByNumero(dbTransaction, resultSet.getInt("FK_VILL")),
                            dbTransaction.getDaoFactory().getDaoRestaurantType().findByNumero(dbTransaction, resultSet.getInt("FK_TYPE")));
                    restaurant.getEvaluations().addAll(dbTransaction.getDaoFactory().getDaoBasicEvaluation().findByNumeroRestaurant(dbTransaction, restaurant));
                    restaurant.getEvaluations().addAll(dbTransaction.getDaoFactory().getDaoCompleteEvaluation().findByNumeroRestaurant(dbTransaction, restaurant));
                    restaurants.add(restaurant);
                }
                return restaurants;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*public int insert(DBTransaction dbTransaction, Restaurant restaurant) {
        try (OraclePreparedStatement pStmt = (OraclePreparedStatement) dbTransaction.getCnn().prepareStatement("INSERT INTO RESTAURANTS (NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL) VALUES (?, ?, ?, ?, ?, ?) RETURNING NUMERO INTO ?")) {
            pStmt.setString(1, restaurant.getName());
            pStmt.setString(2, restaurant.getStreet());
            pStmt.setString(3, restaurant.getDescription());
            pStmt.setString(4, restaurant.getWebsite());
            pStmt.setInt(5, restaurant.getType().getId());
            pStmt.setInt(6, restaurant.getAddress().getCity().getId());
            pStmt.registerReturnParameter(7, OracleTypes.NUMBER);
            pStmt.executeUpdate();
            dbTransaction.getCnn().commit();
            try(ResultSet rs = pStmt.getReturnResultSet()){
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    public int insert(DBTransaction dbTransaction, Restaurant restaurant) {
        try {
            return dbTransaction.functionTransaction(cnn -> {
                try (OraclePreparedStatement pStmt = (OraclePreparedStatement) cnn.prepareStatement("INSERT INTO RESTAURANTS (NOM, ADRESSE, DESCRIPTION, SITE_WEB, FK_TYPE, FK_VILL) VALUES (?, ?, ?, ?, ?, ?) RETURNING NUMERO INTO ?")) {
                    pStmt.setString(1, restaurant.getName());
                    pStmt.setString(2, restaurant.getStreet());
                    pStmt.setString(3, restaurant.getDescription());
                    pStmt.setString(4, restaurant.getWebsite());
                    pStmt.setInt(5, restaurant.getType().getId());
                    pStmt.setInt(6, restaurant.getAddress().getCity().getId());
                    pStmt.registerReturnParameter(7, OracleTypes.NUMBER);
                    pStmt.executeUpdate();
                    try (ResultSet rs = pStmt.getReturnResultSet()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                        return 0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(DBTransaction dbTransaction, Restaurant restaurant) {
        dbTransaction.consumerTransaction(cnn -> {
            try (PreparedStatement pStmt = dbTransaction.getCnn().prepareStatement("UPDATE RESTAURANTS SET NOM = ?, ADRESSE = ?, DESCRIPTION = ?, SITE_WEB = ?, FK_TYPE = ?, FK_VILL = ? WHERE NUMERO = ?")) {
                pStmt.setString(1, restaurant.getName());
                pStmt.setString(2, restaurant.getStreet());
                pStmt.setString(3, restaurant.getDescription());
                pStmt.setString(4, restaurant.getWebsite());
                pStmt.setInt(5, restaurant.getType().getId());
                pStmt.setInt(6, restaurant.getAddress().getCity().getId());
                pStmt.setInt(7, restaurant.getId());
                pStmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void delete(DBTransaction dbTransaction, Restaurant restaurant) {
        dbTransaction.consumerTransaction(cnn -> {
            for (Evaluation eval : restaurant.getEvaluations()) {
                dbTransaction.getDaoFactory().getDaoGrade().delete(dbTransaction, eval.getId());
            }
            dbTransaction.getDaoFactory().getDaoBasicEvaluation().delete(dbTransaction, restaurant);
            dbTransaction.getDaoFactory().getDaoCompleteEvaluation().delete(dbTransaction, restaurant);
            try (PreparedStatement pStmt = dbTransaction.getCnn().prepareStatement("DELETE FROM RESTAURANTS WHERE NUMERO = ?")) {
                pStmt.setInt(1, restaurant.getId());
                pStmt.executeUpdate();
                dbTransaction.getCnn().commit();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
