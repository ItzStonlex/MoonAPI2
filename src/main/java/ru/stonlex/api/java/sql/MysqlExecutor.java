package ru.stonlex.api.java.sql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.stonlex.api.java.JavaMoonAPI;
import ru.stonlex.api.java.interfaces.ResponseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Getter
public class MysqlExecutor {

    /**
     * Опять же, этот код старый, и переписывать его мне было
     * попросту лень, да и тем более, он прекрасно работает.
     *
     * Если кому-то он неудобен, то система как бы не особо сложная,
     * поэтому можно и самому ее написать
     */

    private final MysqlConnection connection;

    static MysqlExecutor getExecutor(MysqlConnection connection) {
        return new MysqlExecutor(connection);
    }

    /**
     * Выполнение SQL запроса
     */
    public void execute(boolean async, String sql, Object... elements) {
        Runnable command = () -> {
            connection.refreshConnection();
            try {
                MysqlStatement mysqlStatement = new MysqlStatement(connection.getConnection(), sql, elements);

                mysqlStatement.execute();
                mysqlStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        if (async) {
            JavaMoonAPI.async(command);
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
                MysqlStatement mysqlStatement = new MysqlStatement(connection.getConnection(), sql, elements);

                result.set(handler.handleResponse(mysqlStatement.getResultSet()));
                mysqlStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        if (async) {
            JavaMoonAPI.async(command);
            return null;
        }

        command.run();

        return result.get();
    }
}
