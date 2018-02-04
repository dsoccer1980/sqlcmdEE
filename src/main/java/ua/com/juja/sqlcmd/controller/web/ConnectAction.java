package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.service.Service;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConnectAction extends AbstractAction {

    public ConnectAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/connect");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            resp.sendRedirect(resp.encodeRedirectURL("menu"));
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            DatabaseManager manager = service.connect(databaseName, userName, password);
            req.getSession().setAttribute("db_manager", manager);
            resp.sendRedirect(resp.encodeRedirectURL("menu"));
        } catch (Exception e) {
            goToJsp(req, resp, "error.jsp");
        }
    }
}
