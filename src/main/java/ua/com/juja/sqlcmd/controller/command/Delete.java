package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class Delete implements Command {

    private DatabaseManager manager;
    private View view;

    public Delete(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 4) {
            throw new IllegalArgumentException(String.format("" +
                    "Формат комманды 'delete|tableName|column|value'," +
                    " а ты прислал: '%s'", command));
        }

        String tableName = data[1];
        List<String> columnAndValue = new LinkedList<>();
        columnAndValue.add(data[2]);
        columnAndValue.add(data[3]);

        try {
            if (!manager.isTableExists(tableName)) {
                throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
            }

            manager.delete(tableName, columnAndValue);
            view.write(String.format("Запись %s в таблице '%s' была успешно удалена.", columnAndValue, tableName));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
