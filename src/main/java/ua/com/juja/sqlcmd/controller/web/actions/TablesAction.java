package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TablesAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/tables");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            req.setAttribute("tables", getService().tables(getManager(req, resp)));
            goToJsp(req, resp, "tables.jsp");
        }
    }
}
