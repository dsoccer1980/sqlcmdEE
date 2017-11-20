package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.List;
import java.util.Set;


public class Find implements Command {
    private DatabaseManager manager;
    private View view;
    private final int COLUMN_WIDTH = 10;
    private final String SEPARATOR_COLUMN = "+--" + new String(new char[COLUMN_WIDTH]).replace("\0", "-") + "--";

    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат комманды 'find|tableName', а ты ввел: " + command);
        }
        String tableName = data[1];

        Set<String> tableColumns = manager.getTableColumns(tableName);
        if (tableColumns.size() != 0) {
            printSeparator(tableColumns.size());
            printHeader(tableColumns);
            printSeparator(tableColumns.size());
            List<DataSet> tableData = manager.getTableData(tableName);
            printTable(tableData);
            printSeparator(tableColumns.size());
        } else {
            throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
        }
    }

    private void printTable(List<DataSet> tableData) {
        for (DataSet row : tableData) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        List<Object> values = row.getValues();
        String formatTableColumn = new String(new char[values.size()]).replace("\0", "+  %-" + COLUMN_WIDTH + "." + COLUMN_WIDTH + "s  ") + "  +";
        view.write(String.format(formatTableColumn, values.toArray()));
    }

    private void printHeader(Set<String> tableColumns) {
        String formatTableColumn = new String(new char[tableColumns.size()]).replace("\0", "+  %-" + COLUMN_WIDTH + "s  ") + "  +";
        view.write(String.format(formatTableColumn, tableColumns.toArray()));
    }

    private void printSeparator(int countColumn) {
        view.write(new String(new char[countColumn]).replace("\0", SEPARATOR_COLUMN) + "--+");
    }

}
