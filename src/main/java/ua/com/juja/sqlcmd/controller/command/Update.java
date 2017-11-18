package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;
import java.sql.SQLException;



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
        if (data.length < 5) {
            throw new IllegalArgumentException(String.format("" +
                    "Формат комманды 'update|tableName|column1|value1|column2|value2|...|columnN|valueN'," +
                    " а ты прислал: '%s'", command));
        }
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException(String.format("Должно быть четное количество параметров " +
                    "в формате 'update|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: '%s'", command));
        }

        String tableName = data[1];
        String columnName = data[2];
        String value = data[3];
        DataSet conditionSet = new DataSetImpl();
        conditionSet.put(columnName, value);

        DataSet dataSet = new DataSetImpl();
        for (int index = 2; index < data.length / 2; index++) {
            columnName = data[index * 2];
            value = data[index * 2 + 1];
            dataSet.put(columnName, value);
        }

        try {
            if (!manager.isTableExists(tableName)) {
                throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
            }

            manager.update(tableName, dataSet, conditionSet);
            view.write(String.format("Запись %s в таблице '%s' была успешно обновлена.", dataSet, tableName));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
