package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.persistence.*;
import ch.hearc.ig.guideresto.business.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) {

    // Différents tests :
    try {
      Connection cnn = DBOracleConnection.openConnection();
      RestaurantMapper rm = new RestaurantMapper();

      /*CityMapper cm = new CityMapper();
      City city = cm.findByName(cnn, "Lausanne");

      RestaurantTypeMapper rtm = new RestaurantTypeMapper();
      RestaurantType restaurantType = rtm.findByName(cnn, "Pizzeria");

      Restaurant restaurant = new Restaurant(null,
              "ChichPizz",
              "Pizzeria des ténèbres",
              "chicpizz.ch",
              "Av. Domino 16",
              city,
              restaurantType);
      rm.insert(cnn, restaurant);*/

      System.out.println(rm.findByName(cnn, "ChichPizz"));

      DBOracleConnection.commit();
      DBOracleConnection.closeConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    /*
    var scanner = new Scanner(System.in);
    var fakeItems = new FakeItems();
    var printStream = System.out;
    var cli = new CLI(scanner, printStream, fakeItems);
    cli.start();
    */

  }

}
