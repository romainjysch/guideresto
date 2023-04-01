package ch.hearc.ig.guideresto.persistence;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

import java.sql.Connection;
import java.util.function.Function;

public class DBTransaction {

    private static final String DBURL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String DBUSER = "romain_jysch";
    private static final String DBPWD = "romain_jysch";

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

    public DBTransaction() {}

    public <T> T functionTransaction(Function<Connection, T> function) {
        Connection connection = openConnection();
        try {
            T result = function.apply(connection);
            connection.commit();
            return result;
        } catch (SQLException exFunction) {
            try {
                connection.rollback();
            } catch (SQLException exRollback) {
                throw new RuntimeException(exRollback);
            }
            throw new RuntimeException(exFunction);
        }
    }

    public void consumerTransaction(Consumer<Connection> function) {
        Connection connection = openConnection();
        try {
            function.accept(connection);
            connection.commit();
        } catch (SQLException exFunction) {
            try {
                connection.rollback();
            } catch (SQLException exRollback) {
                throw new RuntimeException(exRollback);
            }
            throw new RuntimeException(exFunction);
        }
    }

}
