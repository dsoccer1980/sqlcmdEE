package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class TablesTest {
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        View view = mock(View.class);
        command = new Tables(manager, view);
    }

    @Test
    public void testCanProcessTablesWithParametersString() {
        //when
        boolean canProcess = command.canProcess("tables");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessTablesWithWrongParameter() {
        //when
        boolean canProcess = command.canProcess("tables|");

        //then
        assertFalse(canProcess);
    }
}
