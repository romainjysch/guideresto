package ch.hearc.ig.guideresto.services;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.CompleteEvaluation;
import ch.hearc.ig.guideresto.business.EvaluationCriteria;
import ch.hearc.ig.guideresto.business.Grade;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.RestaurantType;
import ch.hearc.ig.guideresto.persistence.DAOFactory;
import ch.hearc.ig.guideresto.persistence.DBOracleConnection;
import ch.hearc.ig.guideresto.persistence.DBTransaction;
import java.util.Set;
import org.w3c.dom.CDATASection;

public class RestaurantService {
    private final DBTransaction dbTransaction;
    private final DAOFactory daoFactory;

    public RestaurantService(DBTransaction dbTransaction, DAOFactory daoFactory) {
        this.dbTransaction = dbTransaction;
        this.daoFactory = daoFactory;
    }
    public void deleteRestaurant(Restaurant restaurant) {
        dbTransaction.consumerTransaction(connection -> daoFactory.getDaoRestaurant().delete(connection, restaurant));
    }

    public Set<Restaurant> findAllRestaurants() {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoRestaurant().findAll(connection));
    }

    public void insertCity(City city) {
        dbTransaction.consumerTransaction(connection -> daoFactory.getDaoCity().insert(connection, city));
    }

    public int findCityByZipCodeAndName(City city) {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoCity().findByZipAndName(connection, city));
    }

    public Set<RestaurantType> findAllRestaurantTypes() {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoRestaurantType().findAll(connection));
    }

    public Set<City> findAllCities() {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoCity().findAll(connection));
    }

    public int insertRestaurant(Restaurant restaurant) {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoRestaurant().insert(connection, restaurant));
    }

    public void insertBasicEvaluation(BasicEvaluation eval) {
        dbTransaction.consumerTransaction(connection -> daoFactory.getDaoBasicEvaluation().insert(connection, eval));
    }

    public int insertCompleteEvaluation(CompleteEvaluation eval) {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoCompleteEvaluation().insert(connection, eval));
    }

    public Set<EvaluationCriteria> findAllEvaluationCriterias() {
        return dbTransaction.functionTransaction(connection -> daoFactory.getDaoEvaluationCriteria().findAll(connection));
    }

    public void insertGrade(Grade grade) {
        dbTransaction.consumerTransaction(connection -> daoFactory.getDaoGrade().insert(connection, grade));
    }

    public void updateRestaurant(Restaurant restaurant) {
        dbTransaction.consumerTransaction(connection -> daoFactory.getDaoRestaurant().update(connection, restaurant));
    }
}
