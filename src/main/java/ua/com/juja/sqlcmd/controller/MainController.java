package ua.com.juja.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(Model model) {
        model.addAttribute("items", service.commandsList());
        return "/menu";
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String tables(HttpSession session, Model model) {
        DatabaseManager manager = getManager(session);
        if (manager == null) {
            return "redirect:connect";
        }
        else {
            model.addAttribute("tables", service.tables(manager));
            return "tables";
        }
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

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

}
