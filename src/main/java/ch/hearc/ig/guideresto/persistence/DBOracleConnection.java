package ch.hearc.ig.guideresto.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBOracleConnection {

    private static final String DBURL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String DBUSER = "romain_jysch";
    private static final String DBPWD = "romain_jysch";

    public static Connection openConnection() {
        try {
            // Se connecte à la BD Oracle :
            // Une seule instance possible de la connection, est-ce une bonne idée?
            // Dans le cas d'une application mono-utilisateur (ligne de commandes), pourquoi pas. Mais
            // dans une application multi-utilisateurs?
            Connection cnn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
            // Désactive le mode autocommit :
            cnn.setAutoCommit(false);
            // Niveau d'isolation de JDBC :
            // Niveau d'isolation déterminé par une constante de l'interface Connection :
            // Est-ce que le niveau d'isolution SERIALIZABLE est un bon défaut ici? Sachant que de toute manière
            // ton applicaton est mono-utilisateur?
            cnn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return cnn;
        } catch (SQLException e) {
            // Afin de ne pas devoir rethrow des SQLException de partout, tu as meilleur temps de rethrow une RuntimeException
            // ici
            throw new RuntimeException(e);
        }
    }
}
