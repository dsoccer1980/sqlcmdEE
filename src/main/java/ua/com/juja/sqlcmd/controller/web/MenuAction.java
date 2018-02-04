package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.service.Service;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MenuAction extends AbstractAction {

    public MenuAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/menu") || url.equals("/");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getManager(req, resp) == null) {
            goToJsp(req, resp, "connect.jsp");
        }
        else {
            req.setAttribute("items", service.commandsList());
            goToJsp(req, resp, "menu.jsp");
        }
    }
}
