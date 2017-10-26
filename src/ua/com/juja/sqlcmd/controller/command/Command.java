package ua.com.juja.sqlcmd.controller.command;

/**
 * Created by denis on 23.10.2017.
 */
public interface Command {
    boolean canProcess(String command);

    void process(String command);
}

