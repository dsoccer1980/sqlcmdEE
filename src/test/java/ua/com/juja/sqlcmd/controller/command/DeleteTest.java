package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class DeleteTest {
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        View view = mock(View.class);
        command = new Delete(manager, view);
    }

    @Test
    public void testCanProcessDeleteWithParametersString() {
        //when
        boolean canProcess = command.canProcess("delete|test_db1|name|pupkin");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessDeleteWithoutParametersString() {
        //when
        boolean canProcess = command.canProcess("delete");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersNotEquals4() {
        //when
        try {
            command.process("delete|test_db1|t");
            fail("Expected error");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат комманды 'delete|tableName|column|value', а ты прислал: 'delete|test_db1|t'", e.getMessage());
        }
    }
}
