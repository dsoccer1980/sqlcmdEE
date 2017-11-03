package ua.com.juja.sqlcmd.model;


import java.util.Set;

public interface DatabaseManager {
    DataSet[] getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String database, String user, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
