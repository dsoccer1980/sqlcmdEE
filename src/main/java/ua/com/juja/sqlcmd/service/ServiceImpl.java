package ua.com.juja.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.UserAction;
import ua.com.juja.sqlcmd.model.UserActionsDao;

import java.util.*;

@Component
public abstract class ServiceImpl implements Service{

    @Autowired
    abstract protected DatabaseManager getManager();

    @Autowired
    private UserActionsDao userActions;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "tables", "create");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        userActions.log(userName, databaseName, "CONNECT");
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

        userActions.log(manager.getUserName(), manager.getDatabaseName(), String.format("FIND(%s)", tableName));

        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        userActions.log(manager.getUserName(), manager.getDatabaseName(), "TABLES");
        return manager.getTableNames();
    }

    @Override
    public List<UserAction> getAll(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("User name can't be null");
        }

        return userActions.getAll(userName);
    }

}
