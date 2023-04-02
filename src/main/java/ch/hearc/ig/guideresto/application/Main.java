package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.persistence.DAOFactory;
import ch.hearc.ig.guideresto.presentation.CLI;
import ch.hearc.ig.guideresto.persistence.DBTransaction;

import ch.hearc.ig.guideresto.services.RestaurantService;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
      try(var dbTransaction = new DBTransaction()) {
          var scanner = new Scanner(System.in);
          var printStream = System.out;
          var daoFactory = new DAOFactory();
          var restaurantService = new RestaurantService(dbTransaction, daoFactory);
          var cli = new CLI(scanner, printStream, restaurantService);
          cli.start();
      }
  }
}
