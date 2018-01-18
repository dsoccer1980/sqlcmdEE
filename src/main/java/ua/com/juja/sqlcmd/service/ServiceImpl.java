package ua.com.juja.sqlcmd.service;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

import java.util.*;

public class ServiceImpl implements Service{

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect", "find");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = new JDBCDatabaseManager();
        manager.connect(databaseName, userName, password);
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

        return result;
    }
}
