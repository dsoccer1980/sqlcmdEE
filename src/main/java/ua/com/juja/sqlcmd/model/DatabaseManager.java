package ua.com.juja.sqlcmd.model;


import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String database, String user, String password);

    void connect(String user, String password);

    void createDatabase(String dbName);

    void dropDatabase(String dbName);

    void clear(String tableName) throws SQLException;

    void insert(String tableName, DataSet input) throws SQLException;

    void update(String tableName, DataSet input, DataSet condition) throws SQLException;

    void create(String tableName, List<String> columnList) throws SQLException;

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    void drop(String tableName) throws SQLException;

    boolean isTableExists(String tableName);

    boolean delete(String tableName, List<String> columnAndValue) throws SQLException;

    boolean isRowExists(String tableName, List<String> columnAndValue);

    void disconnect();

    String getDatabaseName();

    String getUserName();

    void setConnection(Connection connection);

    void setTemplate(JdbcTemplate template);
}
