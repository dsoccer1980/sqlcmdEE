package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

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
        manager.insert(tableName, input);

        //then
        List<DataSet> users = manager.getTableData(tableName);
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, pass, 13]", user.getValues().toString());
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

    @Test
    public void testUpdateData() throws SQLException {
        //given
        String tableName = "users";
        try {
            manager.create(tableName, Arrays.asList("id","name","password"));
        } catch (SQLException e) {

        }
        manager.clear(tableName);

        DataSet input = new DataSetImpl();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");
        manager.insert(tableName,input);

        DataSet updateData = new DataSetImpl();
        updateData.put("name", "StivenNew");
        updateData.put("password", "passNew");

        DataSet conditionData = new DataSetImpl();
        conditionData.put("id", 13);

        //when
        manager.update(tableName, updateData, conditionData);

        //then
        List<DataSet> users = manager.getTableData(tableName);

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[StivenNew, passNew, 13]", user.getValues().toString());
    }
}
