package ru.stonlex.api.test;

import lombok.Getter;
import ru.stonlex.api.java.sql.Executor;
import ru.stonlex.api.java.sql.SQLConnection;

import java.util.function.Consumer;

public class MysqlTest {

    public static final Executor DATABASE = SQLConnection.newBuilder()
            .setHost("localhost")
            .setPort(3306)
            .setUsername("root")
            .setPassword("")
            .setDatabase("database")
            .createTable("TestData", "`Id` INT PRIMARY KEY NOT NULL, `Data` VARCHAR(255) NOT NULL").build().getExecutor();

    private static final String CREATE_DATA_QUERY = "INSERT IGNORE INTO `TestData` (`Id`, `Data`) VALUES (?,?)";
    private static final String UPDATE_DATA_QUERY = "INSERT INTO `TestData` (`Id`, `Data`) VALUES (?,?) ON DUPLICATE KEY UPDATE `Data`=?";
    private static final String GET_DATA_BY_ID_QUERY = "SELECT * FROM `TestData` WHERE `Id`=?";

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
    public void createData(int id, String data) {
        DATABASE.execute(true, CREATE_DATA_QUERY, id, data);
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
