package ua.com.juja.sqlcmd.controller.web;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.controller.web.Action;
import ua.com.juja.sqlcmd.controller.web.actions.*;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionResolver {

    @Autowired
    private Service service;

    private List<Action> actions;

    public ActionResolver() {
        actions  = new ArrayList<>();

//        Reflections reflections = new Reflections(ErrorAction.class.getPackage().getName());
//        Set<Class<? extends AbstractAction>> classes = reflections.getSubTypesOf(AbstractAction.class);
//
//        for (Class<? extends AbstractAction> aClass : classes) {
//            if (aClass.equals(ErrorAction.class)) {
//                continue;
//            }
//            try {
//                AbstractAction action = aClass.getConstructor().newInstance(service.getClass());
//                actions.add(action);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
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
