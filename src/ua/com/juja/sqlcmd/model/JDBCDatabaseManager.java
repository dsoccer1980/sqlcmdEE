package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;


public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;


    @Override
    public DataSet[] getTableData(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName))
        {
            int size = getSize(tableName);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnSize = rsmd.getColumnCount();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= columnSize; i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }

            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet rsCount = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);)
        {
            rsCount.next();
            int size = rsCount.getInt(1);
            return size;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public String[] getTableNames() {
        String sqlSelect = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'";

        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlSelect))
        {
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);

            while (rs.next()) {
                System.out.println(rs.getString("table_name"));
            }

            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
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
            if (connection!=null){
                connection.close();
            }
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + database + "?loggerLevel=OFF", user,
                    password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Cant get connection for model: %s, user: %s, password: %s", database, user, password), e);
        }

    }


    @Override
    public void clear(String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void create(String tableName, DataSet input) {
        try (Statement statement = connection.createStatement()) {
            String tableNames = getNameFormated(input, "%s,");
            String values = getValuesFormated(input, "'%s',");

            String sql = "INSERT INTO " + tableName + " (" + tableNames + ") VALUES(" + values + ")";
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String getValuesFormated(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        String tableNames = getNameFormated(newValue, "%s = ?,");
        String sql = "Update " + tableName + " SET " + tableNames + " Where id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            int index = 1;
            for (Object value : newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }

            ps.setInt(index, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] getTableColumns(String tableName) {
        String sqlSelect = "SELECT * FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName + "'";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sqlSelect))
        {
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("column_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);

            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private String getNameFormated(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
