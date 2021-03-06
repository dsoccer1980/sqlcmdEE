package ua.com.juja.sqlcmd.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.entity.Description;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.*;

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
    public Set<String> tables(HttpServletRequest request) {
        DatabaseManager manager = getManager(request.getSession());

        if (manager == null) {
            return new HashSet<>();
        }

        return service.tables(manager);
    }

    @RequestMapping(value = "/find/{table}/content", method = RequestMethod.GET)
    public List<List<String>> find(@PathVariable(value = "table") String table,
                                   HttpServletRequest request) {
        DatabaseManager manager = getManager(request.getSession());

        if (manager == null) {
            return new LinkedList<>();
        }

        return service.find(manager, table);
    }

    @RequestMapping(value = "/connected", method = RequestMethod.GET)
    public String isConnected(HttpServletRequest request) {
        DatabaseManager manager = getManager(request.getSession());
        return (manager != null) ? manager.getUserName() : null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("db_manager");
    }

    @RequestMapping(value = "/connect", method = RequestMethod.PUT)
    public String connecting(HttpServletRequest request,
                             @ModelAttribute("connection") Connection connection)
    {
        try {
            DatabaseManager manager = service.connect(connection.getDatabase(),
                    connection.getUserName(), connection.getPassword());
            HttpSession session = request.getSession();
            session.setAttribute("db_manager", manager);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/actions/{userName}/content", method = RequestMethod.GET)
    public List<UserActionLog> actions(@PathVariable(value = "userName") String userName) {
        return service.getAllActionsOfUser(userName);
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public String create(HttpServletRequest request,
                             @ModelAttribute("table") Table table)
    {
        String tableName = table.getTableName();
        List<String> columnList = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            columnList.add(request.getParameter(String.format("column%d",index+1)));
        }
        try {
            DatabaseManager manager = getManager(request.getSession());
            if (manager.isTableExists(tableName)) {
                return (String.format("Table %s already exists", tableName));
            }

            service.createTable(manager, tableName, columnList);
            return String.format("Table %s was successfully created", tableName);
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/clear/{tableName}/content", method = RequestMethod.GET)
    public String clear(@PathVariable(value = "tableName") String tableName,
                                   HttpServletRequest request)
    {
        try {
            getManager(request.getSession()).clear(tableName);
            return String.format("Table %s was successfully cleared", tableName);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/drop/{tableName}/content", method = RequestMethod.GET)
    public String drop(@PathVariable(value = "tableName") String tableName,
                        HttpServletRequest request)
    {
        try {
            getManager(request.getSession()).drop(tableName);
            return String.format("Table %s was successfully deleted", tableName);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }
}
