package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class JDBCDatabaseManagerTest {
    private DatabaseManager manager;

    @Before
    public void setup() {
        manager = new JDBCDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");

    }

    @Test
    public void testGetAllTableNames() {
        Set<String> tableNames = manager.getTableNames();
        List<String> tableNamesList = new ArrayList<String>(tableNames);
        Collections.sort(tableNamesList);
        assertEquals("[test, users]", tableNamesList.toString());


    }

    @Test
    public void testGetTableData() throws SQLException {
        //given
        String tableName = "users";
        manager.clear(tableName);

        //when
        DataSet input = new DataSetImpl();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");
        manager.create(tableName, input);

        //then
        List<DataSet> users = manager.getTableData(tableName);
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, pass, 13]", user.getValues().toString());


    }

    @Test
    public void testUpdateTableData() throws SQLException {
        //given
        String tableName = "users";
        manager.clear(tableName);

        DataSet input = new DataSetImpl();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");
        manager.create(tableName, input);


        //when
        DataSet newValue = new DataSetImpl();
        newValue.put("password", "pass2");
        manager.update(tableName, 13, newValue);

        //then
        List<DataSet> users = manager.getTableData(tableName);
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, pass2, 13]", user.getValues().toString());
    }

    @Test
    public void testGetColumnNames() throws SQLException {
        //given
        String tableName = "users";
        manager.clear(tableName);

        //when
        Set<String> columnNames = manager.getTableColumns(tableName);

        assertEquals("[name, password, id]", columnNames.toString());
    }

    @Test
    public void testisConnected() {
        assertTrue(manager.isConnected());

    }
}
