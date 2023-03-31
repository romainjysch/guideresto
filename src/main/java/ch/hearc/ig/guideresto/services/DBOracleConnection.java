package ch.hearc.ig.guideresto.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import javax.sql.PooledConnection;

public class DBOracleConnection {

    private static final String DBURL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String DBUSER = "romain_jysch";
    private static final String DBPWD = "romain_jysch";
    private static final String DRIVERTYPE = "thin";
    private static final String SERVERNAME = "db.ig.he-arc.ch";
    private static final String DBNAME = "ens";
    private static final Integer PORT = 1521;
    private static DataSource ds = null;
    private static Connection cnn = null;
    private static PooledConnection poolConn = null;

    public DBOracleConnection() {}

    public Connection openConnection() {
        try {
            Connection cnn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
            cnn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
            cnn.setAutoCommit(false);
            cnn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return cnn;
        } catch (SQLException exOpenConnection) {
            throw new RuntimeException(exOpenConnection);
        }
    }

    /*public Connection openConnection() {
        try {
            // Création de la DataSource :
            ds = new OracleDataSource();
            // Paramétrage de la DataSource :
            ((OracleDataSource)ds).setDriverType(DRIVERTYPE);
            ((OracleDataSource)ds).setServerName(SERVERNAME);
            ((OracleDataSource)ds).setDatabaseName(DBNAME);
            ((OracleDataSource)ds).setPortNumber(PORT);
            ((OracleDataSource)ds).setUser(DBUSER);
            ((OracleDataSource)ds).setPassword(DBPWD);
            // Partie interface PooledConnection :
            poolConn = ((OracleConnectionPoolDataSource)ds).getPooledConnection();
            // Récupération d'une connexion du pool :
            cnn = poolConn.getConnection();
            // Désactiver l'autocommit :
            cnn.setAutoCommit(false);
            // Niveau d'isolation de JDBC :
            cnn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return cnn;
        } catch (SQLException exOpenPool) {
            throw new RuntimeException(exOpenPool);
        }
    }*/

}
