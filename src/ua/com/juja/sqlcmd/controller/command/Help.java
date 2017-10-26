package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by denis on 23.10.2017.
 */
public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Существующие комманды:");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\t - Подключиться к базе данных, с которой будем работать");

        view.write("\tlist");
        view.write("\t\t - Вывод списка всех таблиц базы данных, к которой подключились");

        view.write("\tclear|tableName"); //TODO переспросить юзер, уверен ли он
        view.write("\t\t - очистка всей таблицы");

        view.write("\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\t - создание записи в таблице");

        view.write("\tfind|tableName");
        view.write("\t\t - Получить содержимое таблицы 'tableName'");

        view.write("\thelp");
        view.write("\t\t - Вывод существующих команд на экран");

        view.write("\texit");
        view.write("\t\t - Выход из программы");
    }
}
