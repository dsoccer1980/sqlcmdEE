package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ClearTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        //when
        command.process("clear|users");

        //then
        verify(manager).clear("users");
        verify(view).write("Таблица users была успешно очищена.");

    }

    @Test
    public void testCanProcessClearWithParametersString() {
        //when
        boolean canProcess = command.canProcess("clear|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessClearWithoutParametersString() {
        //when
        boolean canProcess = command.canProcess("clear");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        //when
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат комманды 'clear|tableName', а ты ввел: clear", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        //when
        try {
            command.process("clear|table|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат комманды 'clear|tableName', а ты ввел: clear|table|qwe", e.getMessage());
        }
    }

}
