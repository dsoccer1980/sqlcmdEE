package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class Update implements Command {

    private DatabaseManager manager;
    private View view;

    public Update(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 6) {
            throw new IllegalArgumentException(String.format("" +
                    "Формат комманды 'update|tableName|column1|value1|column2|value2'," +
                    " а ты прислал: '%s'", command));
        }

        String tableName = data[1];
        List<String> columnsAndValues = new LinkedList<>();
        columnsAndValues.add(data[2]);
        columnsAndValues.add(data[3]);
        columnsAndValues.add(data[4]);
        columnsAndValues.add(data[5]);

        try {
            if (!manager.isTableExists(tableName)) {
                throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
            }

            manager.update(tableName, columnsAndValues);
            view.write(String.format("Запись %s в таблице '%s' была успешно обновлена.", columnsAndValues, tableName));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
