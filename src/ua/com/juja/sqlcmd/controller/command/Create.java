package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by denis on 26.10.2017.
 */
public class Create implements Command {

    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0){
            throw new IllegalArgumentException(String.format("Должно быть четное количество параметров " +
                    "в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: '%s'", command));
        }

        String tableName = data[1];

        DataSet dataSet = new DataSet();
        for (int index = 1; index < data.length / 2; index++) {
            String columnName = data[index * 2];
            String value = data[index * 2 + 1];
            dataSet.put(columnName, value);
        }

        manager.create(tableName, dataSet);
        view.write(String.format("Запись %s в таблице '%s' была успешно создана.",dataSet, tableName));
    }
}
