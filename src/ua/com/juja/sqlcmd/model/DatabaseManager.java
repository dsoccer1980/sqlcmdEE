package ua.com.juja.sqlcmd.model;


import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String database, String user, String password);

    void clear(String tableName) throws SQLException;

    void create(String tableName, DataSet input) throws SQLException;

    void update(String tableName, int id, DataSet newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
