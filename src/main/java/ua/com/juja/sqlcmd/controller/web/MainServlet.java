package ua.com.juja.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.sqlcmd.controller.web.actions.*;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;

    private List<Action> actions = new ArrayList<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        actions.add(new ConnectAction(service));
        actions.add(new MenuAction(service));
        actions.add(new HelpAction(service));
        actions.add(new TablesAction(service));
        actions.add(new FindAction(service));
        actions.add(new ErrorAction(service));
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = getAction(req);
        action.doGet(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Action action = getAction(req);
        action.doPost(req, resp);
    }

    private Action getAction(HttpServletRequest req) {
        String url = getActionName(req);

        for (Action action : actions){
            if (action.canProcess(url)){
                return action;
            }
        }

        return new NullAction(service);
    }

    private String getActionName(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }
}
