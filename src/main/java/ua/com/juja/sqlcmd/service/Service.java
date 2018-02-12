package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.entity.Description;
import ua.com.juja.sqlcmd.model.entity.UserAction;

import java.util.List;
import java.util.Set;

public interface Service {

    List<String> commandsList();

    List<Description> commandsDescription();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> find(DatabaseManager manager, String tableName);

    Set<String> tables(DatabaseManager manager);

    List<UserAction> getAll(String userName);
}
