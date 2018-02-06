package ua.com.juja.sqlcmd.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.controller.Configuration;

import java.sql.*;
import java.util.*;

@Component
public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;
    private Configuration configuration = new Configuration();
    private JdbcTemplate template;

    @Override
    public List<DataSet> getTableData(String tableName) {


        List<DataSet> result = template.query("SELECT * FROM " + tableName,
                new RowMapper<DataSet>() {
                    @Override
                    public DataSet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        DataSet dataSet = new DataSetImpl();
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        int columnSize = rsmd.getColumnCount();
                        for (int i = 0; i < columnSize; i++) {
                            dataSet.put(rsmd.getColumnName(i + 1), resultSet.getObject(i + 1));
                        }
                        return dataSet;
                    }
                });
        return result;
    }

    @Override
    public Set<String> getTableNames() {
        String sqlSelect = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'";
        Set<String> tables = new LinkedHashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sqlSelect)) {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            return tables;
        }
    }

    @Override
    public void connect(String database, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(
                    String.format("%s://%s:%s/%s?loggerLevel=OFF",configuration.getDriver(),configuration.getServerName(),configuration.getPort(),database),
                    user,password);
            template = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Cant get connection for model: %s, user: %s, password: %s. ", database, user, password), e);
        }

    }

    @Override
    public void clear(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + tableName);
        }
    }

    @Override
    public void insert(String tableName, DataSet input) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String tableNames = getNameFormatted(input, "%s,");
            String values = getValuesFormated(input, "'%s',");
            String sql = "INSERT INTO " + tableName + " (" + tableNames + ") VALUES(" + values + ")";

            statement.executeUpdate(sql);
        }
    }

    @Override
    public void update(String tableName, DataSet input, DataSet condition) throws SQLException {
        StringBuilder tableNames = getFormattedStringForSQLQuery(input);
        StringBuilder conditionNames = getFormattedStringForSQLQuery(condition);
        String sql = "Update " + tableName + " SET " + tableNames + " Where " + conditionNames;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private StringBuilder getFormattedStringForSQLQuery(DataSet input) {
        StringBuilder tableNames = new StringBuilder();
        for (String str : input.getNames()) {
            if (str.equals("id")) {
                tableNames.append(String.format("%s = %d,", str, Integer.parseInt(input.get(str).toString())));
            } else {
                tableNames.append(String.format("%s = '%s',", str, input.get(str)));
            }
        }
        tableNames.deleteCharAt(tableNames.length() - 1);
        return tableNames;
    }

    private String getValuesFormated(DataSet input, String format) {
        StringBuilder values = new StringBuilder();
        for (Object value : input.getValues()) {
            values.append(String.format(format, value));
        }
        return values.substring(0, values.length() - 1);
    }

    @Override
    public void drop(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE " + tableName);
        }
    }

    @Override
    public void create(String tableName, List<String> columnList) throws SQLException {
        StringBuilder columnNamesFormatted = new StringBuilder();
        for (String columnName : columnList) {
            if (columnName.equals("id")) {
                columnNamesFormatted.append("id serial,");
            } else {
                columnNamesFormatted.append(columnName).append(" text,");
            }
        }
        columnNamesFormatted.deleteCharAt(columnNamesFormatted.length() - 1);

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("CREATE TABLE " + tableName + "(%s)", columnNamesFormatted));
        }
    }

    @Override
    public boolean delete(String tableName, List<String> columnAndValue) throws SQLException {
        String column = columnAndValue.get(0);
        String value = columnAndValue.get(1);
        try (Statement statement = connection.createStatement()) {
            return (statement.executeUpdate(String.format("DELETE FROM %s WHERE %s='%s'", tableName, column, value)) !=0);
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        String sqlSelect = "SELECT * FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName + "'";
        Set<String> tables = new LinkedHashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sqlSelect)) {
            while (rs.next()) {
                tables.add(rs.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            return tables;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private String getNameFormatted(DataSet newValue, String format) {
        StringBuilder string = new StringBuilder("");
        for (String name : newValue.getNames()) {
            string.append(String.format(format, name));
        }
        return string.substring(0, string.length() - 1);
    }

    @Override
    public boolean isTableExists(String tableName) {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean isRowExists(String tableName, List<String> columnAndValue) {
        List<DataSet> result = new ArrayList<>();

        String column = columnAndValue.get(0);
        String value ="";
        if (column.equals("id")){
            value = "'" + Integer.parseInt(columnAndValue.get(1)) + "'";
        }
        else{
            value = columnAndValue.get(1);
        }
        String sql = String.format("SELECT * FROM %s WHERE %s = %s", tableName, column, value);
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
             return rs.isBeforeFirst();
        } catch (SQLException e) {
            return false;
        }
    }
}
