package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class UpdateTest {
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        View view = mock(View.class);
        command = new Update(manager, view);
    }

    @Test
    public void testCanProcessUpdateWithParametersString() {
        //when
        boolean canProcess = command.canProcess("update|test_db1|id|1|name|pupkin");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessUpdateWithoutParametersString() {
        //when
        boolean canProcess = command.canProcess("update");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersLess5() {
        //when
        try {
            command.process("update|test_db1|t");
            fail("Expected error");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат комманды 'update|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'update|test_db1|t'", e.getMessage());
        }
    }
}
