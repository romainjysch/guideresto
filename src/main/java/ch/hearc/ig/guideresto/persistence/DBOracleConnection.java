package ch.hearc.ig.guideresto.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBOracleConnection {

    private static final String DBURL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String DBUSER = "romain_jysch";
    private static final String DBPWD = "romain_jysch";
    private Connection cnn;

    public DBOracleConnection() {
        this.cnn = openConnection();
    }

    public Connection openConnection() {
        try {
            Connection cnn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
            cnn.setAutoCommit(false);
            cnn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return cnn;
        } catch (SQLException exOpenConnection) {
            throw new RuntimeException(exOpenConnection);
        }
    }

    public Connection getCnn() {
        return cnn;
    }

}
