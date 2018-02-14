package ua.com.juja.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main() {
            return "redirect:menu";
    }

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public String connectGet(HttpSession session, Model model) {
        model.addAttribute("connection", new Connection());

        if (getManager(session) == null) {
            return "connect";
        }
        else {
            return "menu";
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connectPost(@ModelAttribute("connection") Connection connection,
                              HttpSession session) {
        String databaseName = connection.getDbName();
        String userName = connection.getUserName();
        String password = connection.getPassword();

        try {
            DatabaseManager manager = service.connect(databaseName, userName, password);
            session.setAttribute("db_manager", manager);
            return "redirect:menu";
        } catch (Exception e) {
            return "error";
        }
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String tables(HttpSession session, Model model) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return "redirect:connect";
        }

        return "tables";
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find(HttpServletRequest request, HttpSession session, Model model) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return "redirect:connect";
        }
        else {
            String tableName = request.getParameter("table");
            model.addAttribute("table", service.find(getManager(session), tableName));
            return "find";
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createGet(HttpSession session) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return "redirect:connect";
        }
        return "create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPost(HttpServletRequest request, HttpSession session, Model model) {
        String tableName = request.getParameter("tablename");
        List<String> columnList = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            columnList.add(request.getParameter(String.format("column%d",index+1)));
        }
        try {
            DatabaseManager manager = getManager(session);
            if (manager.isTableExists(tableName)) {
                throw new IllegalArgumentException(String.format("Таблица %s уже существует", tableName));
            }
            manager.create(tableName, columnList);
            model.addAttribute("message", String.format("Table %s was successfully created", tableName));
            model.addAttribute("tables", service.tables(manager));
            return "tables";
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(HttpServletRequest request, HttpSession session, Model model) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return "redirect:connect";
        }
        else {
            String tableName = request.getParameter("table");
            try {
                manager.clear(tableName);
                model.addAttribute("message", String.format("Table %s was successfully cleared", tableName));
                model.addAttribute("tables", service.tables(manager));
                return "tables";
            } catch (SQLException e) {
                e.printStackTrace();
                return "menu";
            }
        }
    }

    @RequestMapping(value = "/drop", method = RequestMethod.GET)
    public String drop(HttpServletRequest request, HttpSession session, Model model) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return "redirect:connect";
        } else {
            String tableName = request.getParameter("table");

            try {
                if (!manager.isTableExists(tableName)) {
                    //TODO
                    throw new IllegalArgumentException(String.format("Таблицы %s не существует", tableName));
                }
                manager.drop(tableName);
                model.addAttribute("message", String.format("Table %s was successfully deleted", tableName));
                model.addAttribute("tables", service.tables(manager));
                return "tables";

            } catch (SQLException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/action/{userName}", method = RequestMethod.GET)
    public String actions(Model model,
                         @PathVariable(value = "userName") String userName) {
            model.addAttribute("actions", service.getAll(userName));

            return "actions";

    }


    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

}
