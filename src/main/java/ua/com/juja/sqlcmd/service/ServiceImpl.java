package ua.com.juja.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseConnectionRepository;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.entity.DatabaseConnection;
import ua.com.juja.sqlcmd.model.entity.UserAction;
import ua.com.juja.sqlcmd.model.UserActionRepository;

import java.util.*;

@Component
public abstract class ServiceImpl implements Service{

    @Autowired
    abstract protected DatabaseManager getManager();

    @Autowired
    private UserActionRepository userActions;

    @Autowired
    private DatabaseConnectionRepository databaseConnections;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "tables", "create");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);

        saveAction(databaseName, userName, "CONNECT");
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

        saveAction(manager.getDatabaseName(), manager.getUserName(), String.format("FIND(%s)", tableName));

        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        saveAction(manager.getDatabaseName(), manager.getUserName(), "TABLES");
        return manager.getTableNames();
    }

    @Override
    public List<UserAction> getAll(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("User name can't be null");
        }

        return userActions.findByUserName(userName);
    }

    private void saveAction(String databaseName, String userName, String action) {
        DatabaseConnection databaseConnection = databaseConnections.findByUserNameAndDbName(userName, databaseName);
        if (databaseConnection == null) {
            databaseConnection = databaseConnections.save(new DatabaseConnection(userName, databaseName));
        }
        userActions.save(new UserAction(action, databaseConnection));
    }

}
