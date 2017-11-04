package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class MainController {
    private final Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        commands = new Command[]{
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new isConnected(manager, view),
                new Tables(manager, view),
                new Clear(manager, view),
                new Create(manager, view),
                new Find(manager, view),
                new Unsupported(view)};
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void doWork() {
        view.write("Привет юзер!");
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        return;
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Введи команду или help для помощи:");
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null)
            message += e.getCause().getMessage();
        view.write("Неудача по причине:" + message);
        view.write("Повтори попытку.");
    }

}

