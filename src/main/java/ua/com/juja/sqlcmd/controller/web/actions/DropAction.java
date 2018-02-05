package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;
import ua.com.juja.sqlcmd.model.DatabaseManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DropAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/drop");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            String tableName = req.getParameter("table");

            try {
                DatabaseManager manager = getManager(req, resp);
                if (!manager.isTableExists(tableName)) {
                    //TODO
                    throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
                }
                manager.drop(tableName);
                req.setAttribute("message", String.format("Table %s was successfully deleted", tableName));
                req.setAttribute("tables", getService().tables(getManager(req, resp)));
                goToJsp(req, resp, "tables.jsp");

            } catch (SQLException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }
}
