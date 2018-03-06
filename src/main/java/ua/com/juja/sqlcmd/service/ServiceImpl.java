package ua.com.juja.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.controller.UserActionLog;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.entity.Description;
import ua.com.juja.sqlcmd.model.entity.UserAction;
import ua.com.juja.sqlcmd.model.UserActionRepository;

import java.sql.SQLException;
import java.util.*;

@Component
public abstract class ServiceImpl implements Service{

    @Autowired
    abstract protected DatabaseManager getManager();

    @Autowired
    private UserActionRepository userActions;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "tables", "create", "actions");
    }

    @Override
    public List<Description> commandsDescription() {
        return Arrays.asList(
                new Description("connection", "Для подключения к базе данных"),
                new Description("tables", "Вывод списка всех таблиц базы данных, к которой подключились"),
                new Description("find", "Получить содержимое выбранной таблицы"),
                new Description("action", "Просмотр активностей пользователя"),
                new Description("help", "Список доступных комманд")
        );
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = getManager();
        if (databaseName == null) {
            manager.connect(userName, password);
        }
        else {
            manager.connect(databaseName, userName, password);
        }

        userActions.saveAction(databaseName, userName, "CONNECT");
        return manager;
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();


        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<DataSet> tableData = manager.getTableData(tableName);

        result.add(columns);
        for (DataSet dataSet : tableData){
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns){
                row.add(dataSet.get(column).toString());
            }
        }

        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), String.format("FIND(%s)", tableName));

        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "TABLES");
        return manager.getTableNames();
    }

    @Override
    public void createTable(DatabaseManager manager, String tableName, List<String> columnList) throws SQLException {
            manager.create(tableName, columnList);
        //TODO
      //      userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "CREATE TABLE('" + tableName + "')");
    }

    @Override
    public void createDatabase(DatabaseManager manager, String dbName) {
        manager.createDatabase(dbName);
        //      userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "CREATE DATABASE('" + dbName + "')");
    }

    @Override
    public void dropDatabase(DatabaseManager manager, String dbName) {
        manager.dropDatabase(dbName);
        //      userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "DROP DATABASE('" + dbName + "')");
    }

    @Override
    public List<UserActionLog> getAllActionsOfUser(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("User name cant be null!");
        }

        List<UserAction> actions = userActions.findByUserName(userName);

        List<UserActionLog> result = new LinkedList<>();
        for (UserAction action : actions) {
            result.add(new UserActionLog(action));
        }

        return result;
    }

    @Override
    public void disconnect() {
        getManager().disconnect();
    }

}
