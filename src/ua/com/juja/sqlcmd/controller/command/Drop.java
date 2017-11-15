package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;
import java.sql.SQLException;


public class Drop implements Command {

    private DatabaseManager manager;
    private View view;

    public Drop(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат комманды 'drop|tableName', а ты ввел: " + command);
        }
        String tableName = data[1];
        try {
            if (!manager.isTableExists(tableName)) {
                throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
            }

            String input = getAnswerDeleteTableOrNot(tableName);
            if (input.equals("yes")) {
                manager.drop(tableName);
                view.write(String.format("Таблица %s была успешно удалена.", tableName));
            } else {
                view.write("Команда по удалению таблицы отменена.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String getAnswerDeleteTableOrNot(String tableName) {
        view.write("Вы уверены, что хотите удалить таблицу: " + tableName + ". yes/no?");
        String input = view.read();
        while ((!input.equals("yes")) && (!input.equals("no"))) {
            view.write("Нужно ввести yes или no, а введено: " + input);
            input = view.read();
        }
        return input;
    }
}
