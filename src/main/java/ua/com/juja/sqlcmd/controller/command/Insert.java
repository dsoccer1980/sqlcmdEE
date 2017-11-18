package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;


public class Insert implements Command {

    private DatabaseManager manager;
    private View view;

    public Insert(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length < 3) {
            throw new IllegalArgumentException(String.format("" +
                    "Формат комманды 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN'," +
                    " а ты прислал: '%s'", command));
        }
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException(String.format("Должно быть четное количество параметров " +
                    "в формате 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: '%s'", command));
        }

        String tableName = data[1];

        DataSet dataSet = new DataSetImpl();
        for (int index = 1; index < data.length / 2; index++) {
            String columnName = data[index * 2];
            String value = data[index * 2 + 1];
            dataSet.put(columnName, value);
        }

        try {
            manager.insert(tableName, dataSet);
            view.write(String.format("Запись %s в таблице '%s' была успешно создана.", dataSet, tableName));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
