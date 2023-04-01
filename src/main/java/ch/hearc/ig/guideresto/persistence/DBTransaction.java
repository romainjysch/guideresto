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
    private final Connection connection;

    public DBTransaction() {
        this.connection = openConnection();
    }

    private Connection openConnection() {
        try {
            Connection cnn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
            cnn.setAutoCommit(false);
            cnn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return cnn;
        } catch (SQLException exOpenConnection) {
            throw new RuntimeException(exOpenConnection);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException exCloseConnection) {
            throw new RuntimeException(exCloseConnection);
        }
    }

    public <T> T functionTransaction(Function<Connection, T> function) {
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
