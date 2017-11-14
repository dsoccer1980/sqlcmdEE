package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;


public class Clear implements Command {

    private DatabaseManager manager;
    private View view;

    public Clear(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат комманды 'clear|tableName', а ты ввел: " + command);
        }
        String tableName = data[1];
        try {
            String input = getAnswerClearTableOrNot(tableName);
            if (input.equals("yes")) {
                manager.clear(tableName);
                view.write(String.format("Таблица %s была успешно очищена.", tableName));
            } else {
                view.write("Команда по очистке таблице отменена.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
        }
    }

    private String getAnswerClearTableOrNot(String tableName) {
        view.write("Вы уверены, что хотите очистить таблицу: " + tableName + ". yes/no?");
        String input = view.read();
        while ((!input.equals("yes")) && (!input.equals("no"))) {
            view.write("Нужно ввести yes или no, а введено: " + input);
            input = view.read();
        }
        return input;
    }
}
