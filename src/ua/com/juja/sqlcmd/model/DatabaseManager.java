package ua.com.juja.sqlcmd.model;


import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String database, String user, String password);

    void clear(String tableName) throws SQLException;

    void insert(String tableName, DataSet input) throws SQLException;

    void update(String tableName, int id, DataSet newValue);

    void create(String tableName, List<String> columnList) throws SQLException;

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    void drop(String tableName) throws SQLException;

    boolean isTableExists(String tableName);
}
