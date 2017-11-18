package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;


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
        view.write("\t\t - подключиться к базе данных, с которой будем работать");

        view.write("\ttables");
        view.write("\t\t - вывод списка всех таблиц базы данных, к которой подключились");

        view.write("\tclear|tableName");
        view.write("\t\t - очистка всей таблицы");

        view.write("\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\t - создание записи в таблице");

        view.write("\tcreate|tableName|column1|column2|...|columnN");
        view.write("\t\t - создание таблицы");

        view.write("\tupdate|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\t - обновить запись, установив значение column2 = value2,..,columnN = valueN, для которой соблюдается условие column1 = value1");

        view.write("\tfind|tableName");
        view.write("\t\t - получить содержимое таблицы 'tableName'");

        view.write("\tdrop|tableName");
        view.write("\t\t - удалить таблицу");

        view.write("\tdelete|tableName|column|value");
        view.write("\t\t - удалить запись в таблице");

        view.write("\thelp");
        view.write("\t\t - вывод существующих команд на экран");

        view.write("\texit");
        view.write("\t\t - выход из программы");
    }
}
