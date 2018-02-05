package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ClearAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/clear");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            String tableName = req.getParameter("table");
            try {
                getManager(req, resp).clear(tableName);
                req.setAttribute("message", String.format("Table %s was successfully cleared", tableName));
                req.setAttribute("tables", getService().tables(getManager(req, resp)));
                goToJsp(req, resp, "tables.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
