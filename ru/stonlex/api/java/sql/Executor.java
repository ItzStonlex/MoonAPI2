package ru.stonlex.api.java.sql;

import ru.stonlex.api.java.interfaces.ResponseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Executor {

    private final SQLConnection connection;

    private Executor(final SQLConnection connection) {
        this.connection = connection;
    }

    public static Executor getExecutor(SQLConnection connection) {
        return new Executor(connection);
    }

    /**
     * Выолнение SQL запроса
     */
    public void execute(boolean async, String sql, Object... elements) {
        Runnable command = () -> {
            connection.refreshConnection();
            try {
                Statement statement = new Statement(connection.getConnection(), sql, elements);

                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        if (async) {
            Executors.newCachedThreadPool().submit(command);
            return;
        }

        command.run();
    }

    /**
     * Выолнение SQL запроса с получением ResultSet
     */
    public <T> T executeQuery(boolean async, String sql, ResponseHandler<T, ResultSet, SQLException> handler, Object... elements) {
        AtomicReference<T> result = new AtomicReference<>();

        Runnable command = () -> {
            connection.refreshConnection();
            try {
                Statement statement = new Statement(connection.getConnection(), sql, elements);

                result.set(handler.handleResponse(statement.getResultSet()));
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        if (async) {
            Executors.newCachedThreadPool().submit(command);
            return null;
        }

        command.run();

        return result.get();
    }
}
