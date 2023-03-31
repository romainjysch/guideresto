package ch.hearc.ig.guideresto.services;

import ch.hearc.ig.guideresto.persistence.DAOFactory;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;

import java.sql.Connection;
import java.util.function.Function;

public class DBTransaction {

    private final Connection cnn;
    private final DAOFactory daoFactory;

    public DBTransaction() {
        this.cnn = new DBOracleConnection().openConnection();
        this.daoFactory = new DAOFactory();
    }

    public Connection getCnn() {
        return cnn;
    }

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }

    public void functionTransaction(Function<Connection, Objects> function) {
        try {
            function.apply(this.cnn);
            this.cnn.commit();
        } catch (SQLException exFuntion) {
            try {
                this.cnn.rollback();
            } catch (SQLException exRollback) {
                throw new RuntimeException(exRollback);
            }
            throw new RuntimeException(exFuntion);
        }
    }

    public void consumerTransaction(Consumer<Connection> function) {
        try {
            function.accept(this.cnn);
            this.cnn.commit();
        } catch (SQLException exFuntion) {
            try {
                this.cnn.rollback();
            } catch (SQLException exRollback) {
                throw new RuntimeException(exRollback);
            }
            throw new RuntimeException(exFuntion);
        }
    }

}
