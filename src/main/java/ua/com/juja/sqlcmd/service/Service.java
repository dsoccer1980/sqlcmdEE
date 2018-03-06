package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.controller.UserActionLog;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.entity.Description;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface Service {

    List<String> commandsList();

    List<Description> commandsDescription();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> find(DatabaseManager manager, String tableName);

    Set<String> tables(DatabaseManager manager);

    void createTable(DatabaseManager manager, String tableName, List<String> columnList) throws SQLException;

    void createDatabase(DatabaseManager manager, String dbName);

    void dropDatabase(DatabaseManager manager, String dbName);

    List<UserActionLog> getAllActionsOfUser(String userName);

    void disconnect();
}
