package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class InsertTest {
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        View view = mock(View.class);
        command = new Insert(manager, view);
    }

    @Test
    public void testCanProcessInsertWithParametersString() {
        //when
        boolean canProcess = command.canProcess("insert|test_db1|id|1|name|pupkin");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessInsertWithoutParametersString() {
        //when
        boolean canProcess = command.canProcess("insert");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersLess3() {
        //when
        try {
            command.process("insert|test_db1");
            fail("Expected error");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат комманды 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'insert|test_db1'", e.getMessage());
        }
    }
}
