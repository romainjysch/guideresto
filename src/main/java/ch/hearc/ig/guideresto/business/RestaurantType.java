package ch.hearc.ig.guideresto.business;

import java.util.HashSet;
import java.util.Set;

public class RestaurantType {

    private Integer id;
    private String label;
    private String description;
    private Set<Restaurant> restaurants;

    public RestaurantType(Integer id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.restaurants = new HashSet<>();
    }
    
    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Override
    public String toString() {
        return "RestaurantType{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", restaurants=" + restaurants +
                '}';
    }
}