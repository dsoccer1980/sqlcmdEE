package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;

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
    public void testClearTable() throws SQLException{
        //when
        when(view.read()).thenReturn("yes");
        when(manager.isTableExists("users")).thenReturn(true);
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
            fail("Expected error");
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
            fail("Expected error");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат комманды 'clear|tableName', а ты ввел: clear|table|qwe", e.getMessage());
        }
    }

    @Test
    public void testErrorWhenTableDoesNotExist() {
        //when
        try {
            when(view.read()).thenReturn("yes");
            command.process("clear|badTableName");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Таблицы badTableName не существует", e.getMessage());
        }
    }

}
