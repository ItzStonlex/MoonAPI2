package ru.stonlex.api.test;

import ru.stonlex.api.java.sql.MysqlExecutor;
import ru.stonlex.api.java.sql.MysqlConnection;

import java.util.function.Consumer;

public class MysqlTest {

    public static final MysqlExecutor DATABASE = MysqlConnection.newBuilder()
            .setHost("localhost")
            .setPort(3306)
            .setUsername("root")
            .setPassword("")
            .setDatabase("database")
            .createTable("TestData", "`Id` INT PRIMARY KEY NOT NULL, `Data` TEXT NOT NULL").build().getExecutor();

//----------------------------------------------------------------------------------------------------------------------//
    private static final String GET_DATA_COUNT_QUERY = "SELECT COUNT(*) AS `data_count` `TestData` WHERE `Id`=?";
    private static final String INSERT_DATA_QUERY = "INSERT IGNORE INTO `TestData` (`Id`, `Data`) VALUES (?,?)";
    private static final String UPDATE_DATA_QUERY = INSERT_DATA_QUERY + " ON DUPLICATE KEY UPDATE `Data`=?";
    private static final String GET_DATA_BY_ID_QUERY = "SELECT * FROM `TestData` WHERE `Id`=?";
//----------------------------------------------------------------------------------------------------------------------//

    /**
     * Выполнить запрос об обновлении данных
     *
     * @param id - нумерация данных
     * @param data - данные
     */
    public void updateData(int id, String data) {
        DATABASE.execute(true, UPDATE_DATA_QUERY, id, data, data);
    }

    /**
     * Выполнить запрос о занесении данных в базу данных
     *
     * @param id - нумерация данных
     * @param data - данные
     */
    public void insertData(int id, String data) {
        DATABASE.execute(true, INSERT_DATA_QUERY, id, data);
    }

    /**
     * Получить данные по ее id
     *
     * @param id - нумерация данных
     */
    public String getDataById(int id) {
        return DATABASE.executeQuery(false, GET_DATA_BY_ID_QUERY, rs -> rs.getString("Data"), id);
    }

    /**
     * Получить количество данных
     *
     * @param id - нумерация данных
     */
    public int getDataCount(int id) {
        return DATABASE.executeQuery(false, GET_DATA_COUNT_QUERY, rs -> rs.getInt("data_count"), id);
    }

    /**
     * Асинхронное получение данных по ее id
     *
     * @param id - нумерация данных
     */
    public void getAsyncDataById(int id, Consumer<String> dataConsumer) {
        DATABASE.executeQuery(true, GET_DATA_BY_ID_QUERY, rs -> {
            dataConsumer.accept(rs.getString("Data"));

            return null;
        }, id);
    }

}
