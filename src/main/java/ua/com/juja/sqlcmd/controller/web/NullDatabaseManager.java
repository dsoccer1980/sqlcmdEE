package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NullDatabaseManager implements DatabaseManager {

    @Override
    public List<DataSet> getTableData(String tableName) {
        return new ArrayList<>();
    }

    @Override
    public Set<String> getTableNames() {
        return new HashSet<>();
    }

    @Override
    public void connect(String database, String user, String password) {
        //do nothing
    }

    @Override
    public void clear(String tableName) throws SQLException {
        //do nothing
    }

    @Override
    public void insert(String tableName, DataSet input) throws SQLException {
        //do nothing
    }

    @Override
    public void update(String tableName, DataSet input, DataSet condition) throws SQLException {
        //do nothing
    }

    @Override
    public void create(String tableName, List<String> columnList) throws SQLException {
        //do nothing
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new HashSet<>();
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void drop(String tableName) throws SQLException {
         //do nothing
    }

    @Override
    public boolean isTableExists(String tableName) {
        return false;
    }

    @Override
    public boolean delete(String tableName, List<String> columnAndValue) throws SQLException {
        return false;
    }

    @Override
    public boolean isRowExists(String tableName, List<String> columnAndValue) {
        return false;
    }
}
