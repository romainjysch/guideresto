package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.persistence.DAOBasicEvaluation;
import ch.hearc.ig.guideresto.persistence.DAOCity;
import ch.hearc.ig.guideresto.persistence.DAOCompleteEvaluation;
import ch.hearc.ig.guideresto.persistence.DAOEvaluationCriteria;
import ch.hearc.ig.guideresto.persistence.DAOGrade;
import ch.hearc.ig.guideresto.persistence.DAORestaurant;
import ch.hearc.ig.guideresto.persistence.DAORestaurantType;
import ch.hearc.ig.guideresto.presentation.CLI;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
      var scanner = new Scanner(System.in);
      var printStream = System.out;
      var daoCity = new DAOCity();
      var daoRestaurantType = new DAORestaurantType();
      var daoBasicEvaluation = new DAOBasicEvaluation();
      var daoEvaluationCriteria = new DAOEvaluationCriteria();
      var daoGrade = new DAOGrade(daoEvaluationCriteria);
      var daoCompleteEvaluation = new DAOCompleteEvaluation(daoGrade);
      var daoRestaurant = new DAORestaurant(daoCity, daoRestaurantType, daoBasicEvaluation, daoCompleteEvaluation, daoGrade);
      var cli = new CLI(scanner, printStream, daoRestaurant, daoCity, daoGrade, daoRestaurantType, daoBasicEvaluation,
              daoCompleteEvaluation, daoEvaluationCriteria);
      cli.start();
  }

}
