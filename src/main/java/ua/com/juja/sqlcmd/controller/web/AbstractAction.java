package ua.com.juja.sqlcmd.controller.web;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public abstract class AbstractAction implements Action {

    public  Service service;

    public AbstractAction(Service service) {
        this.service = service;
    }

    protected void goToJsp(ServletRequest req, ServletResponse resp, String path) throws ServletException, IOException {
            req.getRequestDispatcher(path).forward(req, resp);
    }

    protected DatabaseManager getManager(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
//        if (manager == null) {
//            resp.sendRedirect(resp.encodeRedirectURL("connect"));
//            return new NullDatabaseManager();
//        }

        return manager;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }
}
