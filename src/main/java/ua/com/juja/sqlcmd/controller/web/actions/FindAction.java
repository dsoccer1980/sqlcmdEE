package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FindAction extends AbstractAction {

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/find");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            String tableName = req.getParameter("table");
            req.setAttribute("table", getService().find(getManager(req, resp), tableName));
            goToJsp(req, resp, "find.jsp");
        }
    }
}
