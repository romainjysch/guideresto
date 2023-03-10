package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.persistence.CityMapper;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) {
    /*var scanner = new Scanner(System.in);
    var fakeItems = new FakeItems();
    var printStream = System.out;
    var cli = new CLI(scanner, printStream, fakeItems);
    cli.start();
    */
    CityMapper cm = new CityMapper();
    try {
      System.out.println(cm.findByName("Neuchâtel").get(0).getCityName());;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
