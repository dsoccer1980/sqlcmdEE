package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by denis on 02.10.2017.
 */
public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;


    @Override
    public DataSet[] getTableData(String tableName){
        try {
            int size = getSize(tableName);

            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnSize = rsmd.getColumnCount();
            DataSet[]  result = new DataSet[size];
            int index = 0;
            while (rs.next()){
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= columnSize; i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }

            }
            rs.close();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement statement=connection.createStatement();
        ResultSet rsCount = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    @Override
    public  String[] getTableNames(){
        try {
            Statement statement = connection.createStatement();
            String sqlSelect="SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'";
            ResultSet rs = statement.executeQuery(sqlSelect);
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()){
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables,index,String[].class);

            while (rs.next()) {
                System.out.println(rs.getString("table_name"));
            }
            rs.close();
            statement.close();
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
            throw new RuntimeException("Please add jdbc jar to project",e);
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/" + database +"?loggerLevel=OFF", user,
                    password);
        } catch (SQLException e) {
            connection=null;
            throw new RuntimeException(String.format("Cant get connection for model: %s, user: %s, password: %s",database,user,password),e);
        }

    }


    @Override
    public void clear(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + tableName);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void create(String tableName, DataSet input) {
        try {
            Statement statement = connection.createStatement();

            String tableNames = getNameFormated(input, "%s,");
            String values = getValuesFormated(input, "'%s',");

            String sql = "INSERT INTO " + tableName + " (" + tableNames + ") VALUES(" + values +")";
            statement.executeUpdate(sql);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String getValuesFormated(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()){
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        try {

            String tableNames = getNameFormated(newValue, "%s = ?,");


            String sql="Update " + tableName +
                    " SET " + tableNames +  " Where id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            int index = 1;
            for (Object value : newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }

            ps.setInt(index, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] getTableColumns(String tableName) {
        try {
            Statement statement = connection.createStatement();
            String sqlSelect="SELECT * FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName + "'";
            ResultSet rs = statement.executeQuery(sqlSelect);
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()){
                tables[index++] = rs.getString("column_name");
            }
            tables = Arrays.copyOf(tables,index,String[].class);

            rs.close();
            statement.close();
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
        for (String name : newValue.getNames()){
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
