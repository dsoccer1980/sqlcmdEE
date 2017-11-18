package ua.com.juja.sqlcmd.controller.command;


import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.view.View;
import static org.junit.Assert.*;

public class ExitWithMockitoTest {
    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessExitString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessExitQweString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("qwe");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand_ThrowsExitException() {
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing
        }

        //then
        Mockito.verify(view).write("До скорой встречи!");
    }
}
