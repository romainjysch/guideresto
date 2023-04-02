package ch.hearc.ig.guideresto.persistence;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

import java.sql.Connection;
import java.util.function.Function;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;

public class DBTransaction implements AutoCloseable {

    private static final String DBURL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String DBUSER = "romain_jysch";
    private static final String DBPWD = "romain_jysch";

    private static final DataSource dataSource;

    static {
        try {
            // J'utiliserais plutôt HikariCP qui est une implémentation open source, mais c'est
            // pour l'exemple.
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.close();
            oracleDataSource.setUser(DBUSER);
            oracleDataSource.setPassword(DBPWD);
            oracleDataSource.setURL(DBURL);

            // Aucune idée si la configuration est juste, mais pour le principe aussi..
            // Doc: https://docs.oracle.com/cd/B12037_01/java.101/b10979/conncache.htm
            Properties properties = new Properties();
            properties.setProperty("InitialLimit", "10"); // Nombre de connections actives dans le pool à la création
            properties.setProperty("MaxLimit", "300"); // Maximum de connections pouvant être crées
            oracleDataSource.setConnectionProperties(properties);

            dataSource = oracleDataSource;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T functionTransaction(Function<Connection, T> function) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void close() {
        // Aucune idée de comment fermer une OracleDatasource...
    }
}
