package ua.com.juja.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.controller.web.Action;
import ua.com.juja.sqlcmd.controller.web.actions.*;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ActionResolver {

    @Autowired
    private Service service;

    private List<Action> actions = new ArrayList<>();

    public ActionResolver() {
        actions.add(new ConnectAction());
        actions.add(new MenuAction());
        actions.add(new HelpAction());
        actions.add(new TablesAction());
        actions.add(new FindAction());
        actions.add(new ErrorAction());
    }

    public Action getAction(String url) {
        for (Action action : actions){
            if (action.canProcess(url)){
                action.setService(service);
                return action;
            }
        }

        return new NullAction();
    }

    public void setService(Service service) {
        this.service = service;
    }
}
