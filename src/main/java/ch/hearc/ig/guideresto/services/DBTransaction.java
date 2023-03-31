package ch.hearc.ig.guideresto.services;

import ch.hearc.ig.guideresto.persistence.DAOFactory;

import java.sql.SQLException;
import java.util.function.Consumer;

import java.sql.Connection;
import java.util.function.Function;

public class DBTransaction {

    private final DBOracleConnection oracleConnection;
    private final DAOFactory daoFactory;

    public DBTransaction() {
        this.oracleConnection = new DBOracleConnection();
        this.daoFactory = new DAOFactory();
    }

    public DBOracleConnection getOracleConnection() {
        return oracleConnection;
    }

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }

    public int functionTransaction(Function<Connection, Integer> function) {
        try {
            int result = function.apply(oracleConnection.getCnn());
            oracleConnection.getCnn().commit();
            return result;
        } catch (SQLException exFunction) {
            try {
                oracleConnection.getCnn().rollback();
            } catch (SQLException exRollback) {
                throw new RuntimeException(exRollback);
            }
            throw new RuntimeException(exFunction);
        }
    }

    public void consumerTransaction(Consumer<Connection> function) {
        try {
            function.accept(oracleConnection.getCnn());
            oracleConnection.getCnn().commit();
        } catch (SQLException exFunction) {
            try {
                oracleConnection.getCnn().rollback();
            } catch (SQLException exRollback) {
                throw new RuntimeException(exRollback);
            }
            throw new RuntimeException(exFunction);
        }
    }

}
