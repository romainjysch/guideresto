package ch.hearc.ig.guideresto.persistence;

public class DAOFactory {

    private final DAOCity daoCity;
    private final DAORestaurantType daoRestaurantType;
    private final DAOBasicEvaluation daoBasicEvaluation;
    private final DAOEvaluationCriteria daoEvaluationCriteria;
    private final DAOGrade daoGrade;
    private final DAOCompleteEvaluation daoCompleteEvaluation;
    private final DAORestaurant daoRestaurant;

    public DAOFactory() {
        this.daoCity = new DAOCity();
        this.daoRestaurantType = new DAORestaurantType();
        this.daoBasicEvaluation = new DAOBasicEvaluation();
        this.daoEvaluationCriteria = new DAOEvaluationCriteria();
        this.daoGrade = new DAOGrade(daoEvaluationCriteria);
        this.daoCompleteEvaluation = new DAOCompleteEvaluation(daoGrade);
        this.daoRestaurant = new DAORestaurant(daoCity, daoRestaurantType, daoBasicEvaluation, daoCompleteEvaluation, daoGrade);
    }

    public DAOCity getDaoCity() {
        return daoCity;
    }

    public DAORestaurantType getDaoRestaurantType() {
        return daoRestaurantType;
    }

    public DAOBasicEvaluation getDaoBasicEvaluation() {
        return daoBasicEvaluation;
    }

    public DAOEvaluationCriteria getDaoEvaluationCriteria() {
        return daoEvaluationCriteria;
    }

    public DAOGrade getDaoGrade() {
        return daoGrade;
    }

    public DAOCompleteEvaluation getDaoCompleteEvaluation() {
        return daoCompleteEvaluation;
    }

    public DAORestaurant getDaoRestaurant() {
        return daoRestaurant;
    }

}
