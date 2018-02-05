package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/create");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            goToJsp(req, resp, "create.jsp");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tablename");
        List<String> columnList = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            columnList.add(req.getParameter(String.format("column%d",index+1)));
        }
        try {
            if (getManager(req, resp).isTableExists(tableName)) {
                throw new IllegalArgumentException(String.format("Таблица %s уже существует", tableName));
            }
            getManager(req, resp).create(tableName, columnList);
            req.setAttribute("message", String.format("Table %s was successfully created", tableName));
            req.setAttribute("tables", getService().tables(getManager(req, resp)));
            goToJsp(req, resp, "tables.jsp");
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
