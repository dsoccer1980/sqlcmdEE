package ua.com.juja.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;

import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-application-context.xml" })
public class ServiceImplTest {

    @Autowired
    private Service service;

    @Test
    public void test(){
        //given
        DatabaseManager manager = service.connect("sqlcmd", "postgres", "postgres");

        //when
        List<List<String>> users = service.find(manager, "users");

        //then
        assertEquals("[[name, password, id], [denis, ***, 1]]", users.toString());

    }
}
