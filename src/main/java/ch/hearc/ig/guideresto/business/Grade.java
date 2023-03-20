package ch.hearc.ig.guideresto.business;

public class Grade {

    private Integer id;
    private Integer grade;
    private CompleteEvaluation evaluation;
    private EvaluationCriteria criteria;

    public Grade(Integer id, Integer grade, CompleteEvaluation evaluation, EvaluationCriteria criteria) {
        this.id = id;
        this.grade = grade;
        this.evaluation = evaluation;
        this.criteria = criteria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public CompleteEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(CompleteEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public void setCriteria(EvaluationCriteria criteria) {
        this.criteria = criteria;
    }

    public Integer getGrade() {
        return grade;
    }

    public EvaluationCriteria getCriteria() {
        return criteria;
    }
}