package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.persistence.*;
import ch.hearc.ig.guideresto.business.*;

import java.util.Set;

public class MainTest {

    public static void main(String[] args) {
        Set<Restaurant> restaurantSet = DAORestaurant.findAll();
        System.out.println(restaurantSet);
    }

}
