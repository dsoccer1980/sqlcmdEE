package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorAction extends AbstractAction {

    public ErrorAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return true;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "error.jsp");
    }
}