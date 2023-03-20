package ch.hearc.ig.guideresto.business;

public class EvaluationCriteria {

    private Integer id;
    private String name; // nom_utilisateur
    private String description; // commentaire

    public EvaluationCriteria(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

}