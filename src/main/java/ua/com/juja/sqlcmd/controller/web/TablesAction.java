package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TablesAction extends AbstractAction {

    public TablesAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/tables");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tables", service.tables(getManager(req, resp)));
        goToJsp(req, resp, "tables.jsp");
    }
}