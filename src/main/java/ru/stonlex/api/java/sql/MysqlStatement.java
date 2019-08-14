package ru.stonlex.api.java.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final class MysqlStatement {

    /**
     * Опять же, этот код старый, и переписывать его мне было
     * попросту лень, да и тем более, он прекрасно работает.
     *
     * Если кому-то он неудобен, то система как бы не особо сложная,
     * поэтому можно и самому ее написать
     */

    private ResultSet rs;
    private PreparedStatement statement;

    /**
     * Инициализация статемента
     */
    public MysqlStatement(Connection connection, String sql, Object... elements) throws SQLException {
        this.statement = connection.prepareStatement(sql);
        if (elements != null && elements.length != 0) {
            for (int i = 0; i < elements.length; ++i) {
                this.statement.setObject(i + 1, elements[i]);
            }
        }
    }

    /**
     * Выполнение SQL запроса
     */
    public void execute() throws SQLException {
        statement.execute();
    }

    /**
     * Получение ResultSet при помощи выполнения SQL запроса
     */
    public ResultSet getResultSet() throws SQLException {
        return (this.rs = statement.executeQuery());
    }

    /**
     * Закрытие статемента.
     *
     * Нужно после выполнения запроса.
     */
    public void close() throws SQLException {
        if (rs != null && !rs.isClosed()) {
            rs.close();
        }
        if (statement != null && !statement.isClosed()) {
            statement.close();
        }
    }

}
