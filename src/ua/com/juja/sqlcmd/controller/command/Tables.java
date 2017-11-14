package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.*;


public class Tables implements Command {
    private DatabaseManager manager;
    private View view;

    public Tables(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(String command) {
        Set<String> tableNames = manager.getTableNames();
        List<String> tableNamesList = new ArrayList<String>(tableNames);  //TODO
        Collections.sort(tableNamesList);
        String message = tableNamesList.toString();
        view.write(message);
    }
}
