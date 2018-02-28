package ua.com.juja.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.sqlcmd.controller.UserActionLog;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

import java.sql.SQLException;
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
    DatabaseManager manager =new JDBCDatabaseManager();

    @Autowired
    private Service service;

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
