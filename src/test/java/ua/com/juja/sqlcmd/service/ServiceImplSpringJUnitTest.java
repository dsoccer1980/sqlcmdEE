package ua.com.juja.sqlcmd.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-application-context.xml")
public class ServiceImplSpringJUnitTest {

    private static final String DB_NAME = "sqlcmd";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String TEST_DB = "dbfortest";
    DatabaseManager manager = new JDBCDatabaseManager();
    DatabaseManager mockManager = mock(DatabaseManager.class);

    @Autowired
    private Service service;

//    @After
//    public void close() {
//        manager = service.connect(null, DB_USER, DB_PASSWORD);
//        service.dropDatabase(manager, TEST_DB);
//    }

    @Test
    public void testConnect() {
        manager = service.connect(DB_NAME, DB_USER, DB_PASSWORD);
        assertNotNull(manager);
    }

    @Test(expected = Exception.class)
    public void testConnect_WithWrongData() throws Exception {
        service.connect("wrong", "wrong", "wrong");
    }

    @Test
    public void testCreateDatabase() {
        manager = service.connect(null, DB_USER, DB_PASSWORD);
        service.dropDatabase(manager, TEST_DB);

            //manager = service.connect(null, DB_USER, DB_PASSWORD);
            service.createDatabase(manager, TEST_DB);
            manager = service.connect(TEST_DB, DB_USER, DB_PASSWORD);


            assertEquals(TEST_DB, manager.getDatabaseName());

//        manager.setConnection(null);
//        manager.setTemplate(null);


        manager = service.connect(null, DB_USER, DB_PASSWORD);
        service.dropDatabase(manager, TEST_DB);

//        manager.disconnect();
//        manager = service.connect(null, DB_USER, DB_PASSWORD);
//        service.dropDatabase(manager, TEST_DB);
//        try {
//            service.connect(TEST_DB, DB_USER, DB_PASSWORD);
//        } catch (Exception e) {
//            // e.printStackTrace();
//        }
    }

    @Test
    public void testDropDatabase() throws RuntimeException{
        manager = service.connect(null, DB_USER, DB_PASSWORD);
        service.dropDatabase(manager, TEST_DB);
        try {
            service.connect(TEST_DB, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCreateTable() {
        try {
            manager = service.connect(DB_NAME, DB_USER, DB_PASSWORD);
            service.createTable(manager, "test3", Arrays.asList("id", "name", "password"));
            assertEquals("test3", manager.getTableNames());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        //given
        DatabaseManager manager = service.connect("sqlcmd", "postgres", "postgres");
        DataSet input = new DataSetImpl();
        input.put("id", 13);
        input.put("name", "Vasja");
        input.put("password", "Pass");

        DataSet input2 = new DataSetImpl();
        input2.put("id", 14);
        input2.put("name", "Eva");
        input2.put("password", "PassPass");

        try {
            manager.insert("users", input);
            manager.insert("users", input2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //when
        List<List<String>> users = service.find(manager, "users");

        //then
        assertEquals("[[name, password, id], [Vasja, Pass, 13], [Eva, PassPass, 14]]", users.toString());

    }
}
