package ua.com.juja.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.entity.Description;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
public class RestService {

    @Autowired
    private Service service;

    @RequestMapping(value = "/menu/content", method = RequestMethod.GET)
    public List<String> menuItems() {
        return service.commandsList();
    }

    @RequestMapping(value = "/help/content", method = RequestMethod.GET)
    public List<Description> helpItems() {
        return service.commandsDescription();
    }

    @RequestMapping(value = "/tables/content", method = RequestMethod.GET)
    public Set<String> tablesItems(HttpSession session) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return new HashSet<>();
        }
        else {
            return service.tables(manager);
        }
    }

    @RequestMapping(value = "/find/content", method = RequestMethod.GET)
    public List<List<String>> find(HttpServletRequest request, HttpSession session) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return new LinkedList<>();
        }
        else {
            String tableName = request.getParameter("table");
            return service.find(getManager(session), tableName);
        }
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager)session.getAttribute("db_manager");
    }
}
